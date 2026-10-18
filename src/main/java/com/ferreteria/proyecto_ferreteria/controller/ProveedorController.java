package com.ferreteria.proyecto_ferreteria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ferreteria.proyecto_ferreteria.model.Proveedor;
import com.ferreteria.proyecto_ferreteria.service.ProveedorService;
import com.ferreteria.proyecto_ferreteria.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("proveedor")
@RequiredArgsConstructor
public class ProveedorController {

	private final ProveedorService proveedorService;

	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstProveedores", proveedorService.getAll());
		return "proveedor/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("proveedor", new Proveedor());
		return "proveedor/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(@ModelAttribute Proveedor proveedor, RedirectAttributes flash) {
		var response = proveedorService.create(proveedor);
		String icon = response.success() ? "success" : "error";
		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), icon, 4000));

		return "redirect:/proveedor/listado";
	}

	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model) {
		var proveedor = proveedorService.getOne(id);
		if (proveedor == null) {
			flashNotFound(model);
			return "redirect:/proveedor/listado";
		}
		model.addAttribute("proveedor", proveedor);
		return "proveedor/edicion";
	}

	@PostMapping("guardar")
	public String guardar(@ModelAttribute Proveedor proveedor, RedirectAttributes flash) {
		var response = proveedorService.update(proveedor);
		String icon = response.success() ? "success" : "error";
		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), icon, 4000));

		return "redirect:/proveedor/listado";
	}

	@PostMapping("eliminar")
	public String eliminar(@RequestParam Integer id, RedirectAttributes flash) {
		var response = proveedorService.delete(id);
		String icon = response.success() ? "success" : "error";
		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), icon, 4000));

		return "redirect:/proveedor/listado";
	}

	private void flashNotFound(Model model) {
		model.addAttribute("error", "Proveedor no encontrado");
	}
}
