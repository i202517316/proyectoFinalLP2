package com.ferreteria.proyecto_ferreteria.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ferreteria.proyecto_ferreteria.dto.ResultadoResponse;
import com.ferreteria.proyecto_ferreteria.model.Categoria;
import com.ferreteria.proyecto_ferreteria.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {
	
	private final CategoriaRepository categoriaRepository;
	
	public List<Categoria> getAll() {
		return categoriaRepository.findAll();
	}

	public Categoria getOne(Integer id) {
		return categoriaRepository.findById(id).orElse(null);
	}

	public ResultadoResponse create(Categoria categoria) {
		try {
			if (categoria.getIdCategoria() == null) {
				// Buscar el ID máximo actual y sumar 1
				int maxId = categoriaRepository.findAll().stream()
						.mapToInt(Categoria::getIdCategoria)
						.max().orElse(0);
				categoria.setIdCategoria(maxId + 1);
			}
			categoriaRepository.save(categoria);
			return new ResultadoResponse(true, "Categoría registrada exitosamente");
		} catch (Exception e) {
			return new ResultadoResponse(false, "Error al registrar la categoría");
		}
	}

	public ResultadoResponse update(Categoria categoria) {
		try {
			categoriaRepository.save(categoria);
			return new ResultadoResponse(true, "Categoría actualizada exitosamente");
		} catch (Exception e) {
			return new ResultadoResponse(false, "Error al actualizar la categoría");
		}
	}
}
