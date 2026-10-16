
package com.ferreteria.proyecto_ferreteria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ferreteria.proyecto_ferreteria.dto.AutenticacionFilter;
import com.ferreteria.proyecto_ferreteria.model.Usuario;
import com.ferreteria.proyecto_ferreteria.service.AutenticacionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AutenticacionService autenticacionService;

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute(
            "filter",
            new AutenticacionFilter(null, null)
        );

        return "login";
    }

    @GetMapping("/iniciar-sesion")
    public String iniciarSesion(
            @ModelAttribute("filter") AutenticacionFilter filter,
            RedirectAttributes flash,
            HttpSession session) {

        Usuario usuario = autenticacionService.autenticathe(filter);

        // Verificar las credenciales
        if (usuario == null) {
            flash.addFlashAttribute(
                "mensaje",
                "Cuenta o contraseña incorrectas."
            );
            return "redirect:/";
        }

        // Verificar si el usuario está activo
        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            flash.addFlashAttribute(
                "mensaje",
                "Tu usuario está inactivo. Comunícate con el administrador."
            );
            return "redirect:/";
        }

        // Guardar los datos del usuario en la sesión
        session.setAttribute("idUsuario", usuario.getIdUsuario());
        session.setAttribute("fullName", usuario.getFullName());

        if (usuario.getTipo() != null) {
            session.setAttribute(
                "idTipo",
                usuario.getTipo().getIdTipo()
            );
        }

        // Ingresar al Dashboard del proyecto
        return "redirect:/dashboard";
    }

    @GetMapping("/cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
