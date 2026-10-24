package com.ferreteria.proyecto_ferreteria.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ferreteria.proyecto_ferreteria.model.Cliente;
import com.ferreteria.proyecto_ferreteria.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {
	private final ClienteRepository clienteRepository;
	
	public Cliente GetOneByDni(String dni) {
		return clienteRepository.findByDniAndActivoTrue(dni);
	}

	public List<Cliente> getAll() {
		return clienteRepository.findAll();
	}
}
