package com.ferreteria.proyecto_ferreteria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ferreteria.proyecto_ferreteria.model.Categoria;
import com.ferreteria.proyecto_ferreteria.service.CategoriaService;
import com.ferreteria.proyecto_ferreteria.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("categoria")
@RequiredArgsConstructor
public class CategoriaController {

	private final CategoriaService categoriaService;

	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstCategorias", categoriaService.getAll());
		return "categoria/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("categoria", new Categoria());
		return "categoria/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(@ModelAttribute Categoria categoria, RedirectAttributes flash) {
		var response = categoriaService.create(categoria);
		String icon = response.success() ? "success" : "error";
		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), icon, 4000));
		return "redirect:/categoria/listado";
	}

	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model, RedirectAttributes flash) {
		var categoria = categoriaService.getOne(id);
		if (categoria == null) {
			flash.addFlashAttribute("alert", Alert.sweetToast("Categoría no encontrada", "error", 4000));
			return "redirect:/categoria/listado";
		}
		model.addAttribute("categoria", categoria);
		return "categoria/edicion";
	}

	@PostMapping("guardar")
	public String guardar(@ModelAttribute Categoria categoria, RedirectAttributes flash) {
		var response = categoriaService.update(categoria);
		String icon = response.success() ? "success" : "error";
		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), icon, 4000));
		return "redirect:/categoria/listado";
	}
}
