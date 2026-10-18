package com.ferreteria.proyecto_ferreteria.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ferreteria.proyecto_ferreteria.dto.ResultadoResponse;
import com.ferreteria.proyecto_ferreteria.model.Proveedor;
import com.ferreteria.proyecto_ferreteria.repository.ProveedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProveedorService {

	private final ProveedorRepository proveedorRepository;

	public List<Proveedor> getAll() {
		return proveedorRepository.findAllByOrderByIdProveedorDesc();
	}

	public Proveedor getOne(Integer id) {
		return proveedorRepository.findById(id).orElse(null);
	}

	@Transactional
	public ResultadoResponse create(Proveedor proveedor) {
		try {
			if (proveedor.getRazonSocial() == null || proveedor.getRazonSocial().trim().isEmpty()) {
				return new ResultadoResponse(false, "La razón social es obligatoria");
			}

			proveedor.setIdProveedor(null);
			var guardado = proveedorRepository.save(proveedor);
			var mensaje = String.format("Proveedor '%s' registrado exitosamente con ID %d", 
					guardado.getRazonSocial(), guardado.getIdProveedor());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Error al registrar el proveedor: " + e.getMessage());
		}
	}

	@Transactional
	public ResultadoResponse update(Proveedor proveedor) {
		try {
			if (proveedor.getIdProveedor() == null) {
				return new ResultadoResponse(false, "ID de proveedor inválido");
			}

			var guardado = proveedorRepository.save(proveedor);
			var mensaje = String.format("Proveedor con ID %d actualizado exitosamente", guardado.getIdProveedor());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Error al actualizar el proveedor: " + e.getMessage());
		}
	}

	@Transactional
	public ResultadoResponse delete(Integer id) {
		try {
			var proveedor = proveedorRepository.findById(id).orElse(null);
			if (proveedor == null) {
				return new ResultadoResponse(false, "Proveedor no encontrado");
			}
			proveedorRepository.deleteById(id);
			return new ResultadoResponse(true, String.format("Proveedor con ID %d eliminado", id));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "No se puede eliminar el proveedor porque tiene productos asociados.");
		}
	}
}
