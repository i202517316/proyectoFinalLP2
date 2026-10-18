package com.ferreteria.proyecto_ferreteria.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
}
