package com.ferreteria.proyecto_ferreteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ferreteria.proyecto_ferreteria.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	Cliente findByDniAndActivoTrue(String dni);
}
