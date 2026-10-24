package com.ferreteria.proyecto_ferreteria.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ferreteria.proyecto_ferreteria.dto.BoletaFilter;
import com.ferreteria.proyecto_ferreteria.dto.ProductoSeleccionado;
import com.ferreteria.proyecto_ferreteria.model.Boleta;
import com.ferreteria.proyecto_ferreteria.model.Cliente;
import com.ferreteria.proyecto_ferreteria.model.DetBoleta;
import com.ferreteria.proyecto_ferreteria.model.DetBoletaId;
import com.ferreteria.proyecto_ferreteria.model.Usuario;
import com.ferreteria.proyecto_ferreteria.service.BoletaService;
import com.ferreteria.proyecto_ferreteria.service.ClienteService;
import com.ferreteria.proyecto_ferreteria.service.ProductoService;
import com.ferreteria.proyecto_ferreteria.service.UsuarioService;
import com.ferreteria.proyecto_ferreteria.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("boleta")
@RequiredArgsConstructor
@SessionAttributes({ "seleccionados", "cliente" })
public class BoletaController {

	private final ProductoService productoService;
	private final ClienteService clienteService;
	private final UsuarioService usuarioService;
	private final BoletaService boletaService;

	@ModelAttribute("seleccionados")
	public List<ProductoSeleccionado> inicio() {
		return new ArrayList<ProductoSeleccionado>();
	}

	@GetMapping("listado")
	public String listado(@ModelAttribute BoletaFilter filter, Model model, HttpSession session) {
		Integer idTipo = (Integer) session.getAttribute("idTipo");
		if (idTipo == null) idTipo = (Integer) session.getAttribute("ID_TIPO");

		if (idTipo != null && idTipo != 1) {
			Integer idUsuario = (Integer) session.getAttribute("idUsuario");
			if (idUsuario == null) idUsuario = (Integer) session.getAttribute("ID_USUARIO");
			filter = filter.withIdUsuario(idUsuario);
		}

		model.addAttribute("lstBoletas", boletaService.search(filter));
		return "boleta/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("productos", productoService.getAllActive());
		model.addAttribute("productoSeleccionado", new ProductoSeleccionado());
		return "boleta/nuevo";
	}

	@GetMapping("/validar-cliente")
	public String validarCliente(@RequestParam String accion, @RequestParam String dni, Model model,
			RedirectAttributes flash, HttpSession session) {

		if ("limpiar".equals(accion)) {
			model.addAttribute("cliente", null);
			session.removeAttribute("cliente");
			return "redirect:/boleta/nuevo";
		}

		Cliente cliente = clienteService.GetOneByDni(dni);

		if (cliente == null) {
			flash.addFlashAttribute("alert", Alert.sweetAlertInfo("Cliente no encontrado con DNI: " + dni));
			return "redirect:/boleta/nuevo";
		}

		model.addAttribute("cliente", cliente);
		return "redirect:/boleta/nuevo";
	}

	@PostMapping("agregar")
	public String agregar(@ModelAttribute ProductoSeleccionado productoSeleccionado,
			@ModelAttribute("seleccionados") List<ProductoSeleccionado> seleccionados, RedirectAttributes flash) {

		boolean existe = seleccionados.stream()
				.anyMatch(p -> p.getIdProducto().equals(productoSeleccionado.getIdProducto()));

		if (existe) {
			flash.addFlashAttribute("alert", Alert.sweetAlertInfo("El producto ya se encuentra en la lista"));
			return "redirect:/boleta/nuevo";
		}

		seleccionados.add(productoSeleccionado);
		return "redirect:/boleta/nuevo";
	}

	@PostMapping("remover")
	public String remover(@RequestParam Integer id,
			@ModelAttribute("seleccionados") List<ProductoSeleccionado> seleccionados, Model model) {

		seleccionados.removeIf(p -> p.getIdProducto().equals(id));
		return "redirect:/boleta/nuevo";
	}

	@PostMapping("/registrar")
	public String registrar(@ModelAttribute Boleta boleta,
			@ModelAttribute("seleccionados") List<ProductoSeleccionado> seleccionados,
			RedirectAttributes flash, HttpSession session, SessionStatus sessionStatus) {

		Integer idUsuario = (Integer) session.getAttribute("idUsuario");
		if (idUsuario == null) idUsuario = (Integer) session.getAttribute("ID_USUARIO");

		if (idUsuario == null) {
			flash.addFlashAttribute("alert", Alert.sweetAlertInfo("Sesión caducada"));
			return "redirect:/";
		}

		Usuario usuario = usuarioService.getOne(idUsuario);
		boleta.setUsuario(usuario);

		Cliente cliente = (Cliente) session.getAttribute("cliente");

		if (cliente == null) {
			flash.addFlashAttribute("alert", Alert.sweetAlertInfo("Por favor, busque y seleccione a un cliente por su DNI"));
			return "redirect:/boleta/nuevo";
		}

		boleta.setCliente(cliente);

		if (seleccionados == null || seleccionados.isEmpty()) {
			flash.addFlashAttribute("alert", Alert.sweetAlertInfo("Debe agregar al menos un producto a la boleta"));
			return "redirect:/boleta/nuevo";
		}

		var lstDetBoleta = seleccionados.stream().map(
			item -> {
				var detBoletaId = new DetBoletaId(boleta, productoService.getOne(item.getIdProducto()));
				return new DetBoleta(detBoletaId, item.getCantidad(), item.getPrecio());
			}
		).toList();

		boleta.setLstDetBoleta(lstDetBoleta);

		var response = boletaService.create(boleta);

		if (response.success()) {
			sessionStatus.setComplete();
		}

		flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), "success", 5000));
		return "redirect:/boleta/listado";
	}
}
