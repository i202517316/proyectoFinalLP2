package com.ferreteria.proyecto_ferreteria.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ferreteria.proyecto_ferreteria.dto.BoletaFilter;
import com.ferreteria.proyecto_ferreteria.dto.ResultadoResponse;
import com.ferreteria.proyecto_ferreteria.model.Boleta;
import com.ferreteria.proyecto_ferreteria.repository.BoletaRepository;
import com.ferreteria.proyecto_ferreteria.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoletaService {
	
	private final BoletaRepository boletaRepository;
	private final ProductoRepository productoRepository;
	
	public List<Boleta> search(BoletaFilter filter) {
		Integer idUsuario = (filter != null) ? filter.idUsuario() : null;
		return boletaRepository.findByFilters(idUsuario);
	}
	
	@Transactional
	public ResultadoResponse create(Boleta boleta) {
		try {
			if (boleta.getLstDetBoleta() != null) {
				for (var det : boleta.getLstDetBoleta()) {
					var producto = det.getId().getProducto();
					if (producto != null) {
						int nuevoStock = producto.getStock() - det.getCantidad();
						producto.setStock(Math.max(nuevoStock, 0));
						productoRepository.save(producto);
					}
				}
			}
			
			var registrado = boletaRepository.save(boleta);
			var mensaje = String.format("Boleta con Nro. %d registrada exitosamente", registrado.getNumBoleta());
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Error al registrar la boleta: " + e.getMessage());
		}
	}
}
