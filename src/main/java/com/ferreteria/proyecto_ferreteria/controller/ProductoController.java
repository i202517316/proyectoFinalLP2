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

import com.ferreteria.proyecto_ferreteria.dto.ProductoFilter;
import com.ferreteria.proyecto_ferreteria.model.Producto;
import com.ferreteria.proyecto_ferreteria.service.CategoriaService;
import com.ferreteria.proyecto_ferreteria.service.ProductoService;
import com.ferreteria.proyecto_ferreteria.service.ProveedorService;
import com.ferreteria.proyecto_ferreteria.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("producto")
@RequiredArgsConstructor
public class ProductoController {

	private final ProductoService productoService;
	private final CategoriaService categoriaService;
	private final ProveedorService proveedorService;

	@GetMapping("listado")
	public String listado(@ModelAttribute("filter") ProductoFilter filter, Model model) {
		model.addAttribute("lstProductos", productoService.search(filter));
		model.addAttribute("lstCategorias", categoriaService.getAll());
		model.addAttribute("lstProveedores", proveedorService.getAll());
		model.addAttribute("filter", filter);
		return "producto/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("lstCategorias", categoriaService.getAll());
		model.addAttribute("lstProveedores", proveedorService.getAll());
		model.addAttribute("producto", new Producto());
		return "producto/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(@ModelAttribute Producto producto, RedirectAttributes flash) {
		var response = productoService.create(producto);
		String icon = response.success() ? "success" : "error";
		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), icon, 4000));

		return "redirect:/producto/listado";
	}

	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model) {
		model.addAttribute("lstCategorias", categoriaService.getAll());
		model.addAttribute("lstProveedores", proveedorService.getAll());
		model.addAttribute("producto", productoService.getOne(id));
		return "producto/edicion";
	}

	@PostMapping("guardar")
	public String guardar(@ModelAttribute Producto producto, RedirectAttributes flash) {
		var response = productoService.update(producto);
		String icon = response.success() ? "success" : "error";
		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), icon, 4000));

		return "redirect:/producto/listado";
	}

	@PostMapping("cambiar-activo")
	public String cambiarEstado(@RequestParam Integer id, RedirectAttributes flash) {
		var response = productoService.changeActive(id);
		String icon = response.success() ? "success" : "error";
		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), icon, 4000));

		return "redirect:/producto/listado";
	}
}
