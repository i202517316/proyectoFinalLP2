package com.ferreteria.proyecto_ferreteria.service;

import org.springframework.stereotype.Service;
import com.ferreteria.proyecto_ferreteria.dto.AutenticacionFilter;
import com.ferreteria.proyecto_ferreteria.model.Usuario;
import com.ferreteria.proyecto_ferreteria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacionService {

    private final UsuarioRepository usuarioRepository;

    public Usuario autenticathe(AutenticacionFilter filter) {
        return usuarioRepository.findByCuentaAndClave(
            filter.cuenta(),
            filter.clave()
        );
    }
}