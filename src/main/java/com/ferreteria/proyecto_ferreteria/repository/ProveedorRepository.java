package com.ferreteria.proyecto_ferreteria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ferreteria.proyecto_ferreteria.model.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

	List<Proveedor> findAllByOrderByIdProveedorDesc();
}
