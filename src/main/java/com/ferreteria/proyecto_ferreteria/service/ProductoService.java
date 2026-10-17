
package com.ferreteria.proyecto_ferreteria.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ferreteria.proyecto_ferreteria.dto.ProductoFilter;
import com.ferreteria.proyecto_ferreteria.model.Producto;
import com.ferreteria.proyecto_ferreteria.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Obtener todos los productos
    @Transactional(readOnly = true)
    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    // Buscar productos utilizando filtros opcionales
    @Transactional(readOnly = true)
    public List<Producto> search(ProductoFilter filter) {

        String descripcion = filter.getDescripcion();

        if (descripcion != null) {
            descripcion = descripcion.trim();

            if (descripcion.isEmpty()) {
                descripcion = null;
            }
        }

        return productoRepository.buscarProductos(
                descripcion,
                filter.getIdCategoria(),
                filter.getActivo()
        );
    }

    // Obtener un producto por su identificador
    @Transactional(readOnly = true)
    public Producto getOne(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Producto no encontrado: " + id
                    )
                );
    }

    // Registrar un nuevo producto
    @Transactional
    public Producto create(Producto producto) {

        validarProducto(producto);

        producto.setIdProducto(null);

        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }

        return productoRepository.save(producto);
    }

    // Actualizar un producto existente
    @Transactional
    public Producto update(Integer id, Producto datos) {

        Producto producto = getOne(id);

        validarProducto(datos);

        producto.setDescripcion(datos.getDescripcion());
        producto.setStock(datos.getStock());
        producto.setPrecio(datos.getPrecio());
        producto.setCategoria(datos.getCategoria());
        producto.setProveedor(datos.getProveedor());

        return productoRepository.save(producto);
    }

    // Cambiar el estado activo / inactivo
    @Transactional
    public Producto changeActive(Integer id) {

        Producto producto = getOne(id);

        producto.setActivo(
                !Boolean.TRUE.equals(producto.getActivo())
        );

        return productoRepository.save(producto);
    }

    // Validaciones de la lógica de negocio
    private void validarProducto(Producto producto) {

        if (producto == null) {
            throw new IllegalArgumentException(
                "Los datos del producto son obligatorios"
            );
        }

        if (producto.getDescripcion() == null
                || producto.getDescripcion().trim().isEmpty()) {

            throw new IllegalArgumentException(
                "La descripción es obligatoria"
            );
        }

        producto.setDescripcion(
                producto.getDescripcion().trim()
        );

        if (producto.getDescripcion().length() > 45) {
            throw new IllegalArgumentException(
                "La descripción no puede superar 45 caracteres"
            );
        }

        if (producto.getStock() == null
                || producto.getStock() < 0) {

            throw new IllegalArgumentException(
                "El stock no puede ser negativo"
            );
        }

        if (producto.getPrecio() == null
                || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                "El precio debe ser mayor que cero"
            );
        }
    }
}
