package com.ferreteria.proyecto_ferreteria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ferreteria.proyecto_ferreteria.dto.AutenticacionFilter;
import com.ferreteria.proyecto_ferreteria.service.AutenticacionService;
import com.ferreteria.proyecto_ferreteria.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	private final AutenticacionService autenticacionService;

	@GetMapping("/")
	public String root(Model model) {
		model.addAttribute("filter", new AutenticacionFilter(null, null));
		return "login";
	}

	@GetMapping("iniciar-sesion")
	public String iniciarSesion(@ModelAttribute AutenticacionFilter filter, RedirectAttributes flash,
			HttpSession session) {

		var usuario = autenticacionService.autenticathe(filter);

		// validamos si las credenciales son correctas
		if (usuario == null) {
			var alert = Alert.sweetAlertError("Cuenta y/o clave inválidos");
			flash.addFlashAttribute("alert", alert);
			return "redirect:/";
		}

		// validamos si el usuario está activo
		if (!Boolean.TRUE.equals(usuario.getActivo())) {
			var alert = Alert.sweetAlertError("Usuario inactivado, comunicarse con el administrador");
			flash.addFlashAttribute("alert", alert);
			return "redirect:/";
		}

		session.setAttribute("idUsuario", usuario.getIdUsuario());
		session.setAttribute("ID_USUARIO", usuario.getIdUsuario());
		session.setAttribute("fullName", usuario.getFullName());
		session.setAttribute("FULL_NAME", usuario.getFullName());
		if (usuario.getTipo() != null) {
			session.setAttribute("idTipo", usuario.getTipo().getIdTipo());
			session.setAttribute("ID_TIPO", usuario.getTipo().getIdTipo());
		}

		String alert = Alert.sweetAlertSuccess("Bienvenido a FerreteríaWeb, " + usuario.getFullName());
		flash.addFlashAttribute("alert", alert);

		return "redirect:/home/dashboard";
	}

	@GetMapping("cerrar-sesion")
	public String cerrarSesion(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
