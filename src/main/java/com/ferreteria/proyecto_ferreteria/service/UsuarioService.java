package com.ferreteria.proyecto_ferreteria.service;

import org.springframework.stereotype.Service;

import com.ferreteria.proyecto_ferreteria.model.Usuario;
import com.ferreteria.proyecto_ferreteria.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {
	private final UsuarioRepository usuarioRepository;

	public Usuario getOne(Integer id) {
		return usuarioRepository.findById(id).orElse(null);
	}
}
