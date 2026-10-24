package com.ferreteria.proyecto_ferreteria.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ferreteria.proyecto_ferreteria.dto.ProductoFilter;
import com.ferreteria.proyecto_ferreteria.dto.ResultadoResponse;
import com.ferreteria.proyecto_ferreteria.model.Producto;
import com.ferreteria.proyecto_ferreteria.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

	private final ProductoRepository productoRepository;

	@Transactional(readOnly = true)
	public List<Producto> getAll() {
		return productoRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Producto> getAllActive() {
		return productoRepository.findAllByActivoTrue();
	}

	@Transactional(readOnly = true)
	public List<Producto> search(ProductoFilter filter) {
		String descripcion = null;
		Integer idCategoria = null;
		Boolean activo = null;

		if (filter != null) {
			if (filter.getDescripcion() != null && !filter.getDescripcion().trim().isEmpty()) {
				descripcion = filter.getDescripcion().trim();
			}
			idCategoria = filter.getIdCategoria();
			activo = filter.getActivo();
		}

		return productoRepository.buscarProductos(descripcion, idCategoria, activo);
	}

	@Transactional(readOnly = true)
	public Producto getOne(Integer id) {
		return productoRepository.findById(id).orElse(null);
	}

	@Transactional
	public ResultadoResponse create(Producto producto) {
		try {
			validarProducto(producto);
			producto.setIdProducto(null);
			if (producto.getActivo() == null) {
				producto.setActivo(true);
			}

			var guardado = productoRepository.save(producto);
			var mensaje = String.format("Producto '%s' registrado exitosamente con ID %d", 
					guardado.getDescripcion(), guardado.getIdProducto());

			return new ResultadoResponse(true, mensaje);
		} catch (IllegalArgumentException e) {
			return new ResultadoResponse(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Error al registrar el producto: " + e.getMessage());
		}
	}

	@Transactional
	public ResultadoResponse update(Producto producto) {
		try {
			if (producto.getIdProducto() == null) {
				return new ResultadoResponse(false, "El ID del producto es obligatorio");
			}

			validarProducto(producto);

			var existente = getOne(producto.getIdProducto());
			if (existente == null) {
				return new ResultadoResponse(false, "Producto no encontrado");
			}

			existente.setDescripcion(producto.getDescripcion());
			existente.setStock(producto.getStock());
			existente.setPrecio(producto.getPrecio());
			existente.setCategoria(producto.getCategoria());
			existente.setProveedor(producto.getProveedor());

			productoRepository.save(existente);
			var mensaje = String.format("Producto con ID %d actualizado exitosamente", producto.getIdProducto());

			return new ResultadoResponse(true, mensaje);
		} catch (IllegalArgumentException e) {
			return new ResultadoResponse(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Error al actualizar el producto: " + e.getMessage());
		}
	}

	@Transactional
	public ResultadoResponse changeActive(Integer id) {
		try {
			var producto = getOne(id);
			if (producto == null) {
				return new ResultadoResponse(false, "Producto no encontrado");
			}

			producto.setActivo(!Boolean.TRUE.equals(producto.getActivo()));
			productoRepository.save(producto);

			var estado = Boolean.TRUE.equals(producto.getActivo()) ? "activado" : "inactivado";
			var mensaje = String.format("Producto con ID %d %s correctamente", id, estado);

			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error al cambiar el estado del producto");
		}
	}

	private void validarProducto(Producto producto) {
		if (producto == null) {
			throw new IllegalArgumentException("Los datos del producto son obligatorios");
		}
		if (producto.getDescripcion() == null || producto.getDescripcion().trim().isEmpty()) {
			throw new IllegalArgumentException("La descripción es obligatoria");
		}
		if (producto.getDescripcion().trim().length() > 45) {
			throw new IllegalArgumentException("La descripción no puede superar 45 caracteres");
		}
		if (producto.getStock() == null || producto.getStock() < 0) {
			throw new IllegalArgumentException("El stock no puede ser negativo");
		}
		if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("El precio debe ser mayor que cero");
		}
	}
}
