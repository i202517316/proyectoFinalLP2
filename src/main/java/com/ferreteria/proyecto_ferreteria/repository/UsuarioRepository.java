package com.ferreteria.proyecto_ferreteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ferreteria.proyecto_ferreteria.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
	Usuario findByCuentaAndClave(String cuenta, String clave);
}
