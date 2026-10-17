
package com.ferreteria.proyecto_ferreteria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ferreteria.proyecto_ferreteria.dto.ProductoFilter;
import com.ferreteria.proyecto_ferreteria.model.Categoria;
import com.ferreteria.proyecto_ferreteria.model.Producto;
import com.ferreteria.proyecto_ferreteria.model.Proveedor;
import com.ferreteria.proyecto_ferreteria.repository.CategoriaRepository;
import com.ferreteria.proyecto_ferreteria.repository.ProveedorRepository;
import com.ferreteria.proyecto_ferreteria.service.ProductoService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/producto")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;

    // =====================================================
    // METODOS AUXILIARES
    // =====================================================

    // Verificar si existe una sesion iniciada
    private boolean sinSesion(HttpSession session) {
        return session.getAttribute("idUsuario") == null;
    }

    // Cargar las categorias y proveedores
    private void cargarCombos(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findAll());
    }

    // Validar y recuperar las relaciones seleccionadas
    private void asignarRelaciones(Producto producto) {

        if (producto.getCategoria() != null
                && producto.getCategoria().getIdCategoria() != null) {

            Integer idCategoria =
                    producto.getCategoria().getIdCategoria();

            Categoria categoria = categoriaRepository.findById(idCategoria)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "La categoria seleccionada no existe."
                    ));

            producto.setCategoria(categoria);

        } else {
            producto.setCategoria(null);
        }

        if (producto.getProveedor() != null
                && producto.getProveedor().getIdProveedor() != null) {

            Integer idProveedor =
                    producto.getProveedor().getIdProveedor();

            Proveedor proveedor = proveedorRepository.findById(idProveedor)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El proveedor seleccionado no existe."
                    ));

            producto.setProveedor(proveedor);

        } else {
            producto.setProveedor(null);
        }
    }

    // =====================================================
    // LISTADO Y BUSQUEDA
    // =====================================================

    @GetMapping("/listado")
    public String listado(
            @ModelAttribute("filter") ProductoFilter filter,
            Model model,
            HttpSession session) {

        if (sinSesion(session)) {
            return "redirect:/";
        }

        model.addAttribute(
                "productos",
                productoService.search(filter)
        );

        model.addAttribute(
                "categorias",
                categoriaRepository.findAll()
        );

        return "producto/listado";
    }

    // =====================================================
    // NUEVO PRODUCTO
    // =====================================================

    @GetMapping("/nuevo")
    public String nuevo(Model model, HttpSession session) {

        if (sinSesion(session)) {
            return "redirect:/";
        }

        model.addAttribute("producto", new Producto());

        cargarCombos(model);

        return "producto/nuevo";
    }

    // =====================================================
    // REGISTRAR PRODUCTO
    // =====================================================

    @PostMapping("/registrar")
    public String registrar(
            @ModelAttribute("producto") Producto producto,
            Model model,
            RedirectAttributes flash,
            HttpSession session) {

        if (sinSesion(session)) {
            return "redirect:/";
        }

        try {

            asignarRelaciones(producto);

            productoService.create(producto);

            flash.addFlashAttribute(
                    "mensaje",
                    "Producto registrado correctamente."
            );

            return "redirect:/producto/listado";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());
            model.addAttribute("producto", producto);

            cargarCombos(model);

            return "producto/nuevo";
        }
    }

    // =====================================================
    // FORMULARIO DE EDICION
    // =====================================================

    @GetMapping("/edicion/{id}")
    public String edicion(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes flash,
            HttpSession session) {

        if (sinSesion(session)) {
            return "redirect:/";
        }

        try {

            Producto producto = productoService.getOne(id);

            model.addAttribute("producto", producto);

            cargarCombos(model);

            return "producto/edicion";

        } catch (IllegalArgumentException e) {

            flash.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/producto/listado";
        }
    }

    // =====================================================
    // ACTUALIZAR PRODUCTO
    // =====================================================

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute("producto") Producto producto,
            Model model,
            RedirectAttributes flash,
            HttpSession session) {

        if (sinSesion(session)) {
            return "redirect:/";
        }

        Integer id = producto.getIdProducto();

        if (id == null) {

            flash.addFlashAttribute(
                    "error",
                    "El identificador del producto es obligatorio."
            );

            return "redirect:/producto/listado";
        }

        try {

            asignarRelaciones(producto);

            productoService.update(id, producto);

            flash.addFlashAttribute(
                    "mensaje",
                    "Producto actualizado correctamente."
            );

            return "redirect:/producto/listado";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());
            model.addAttribute("producto", producto);

            cargarCombos(model);

            return "producto/edicion";
        }
    }

    // =====================================================
    // ACTIVAR O DESACTIVAR PRODUCTO
    // =====================================================

    @PostMapping("/cambiar-activo/{id}")
    public String cambiarActivo(
            @PathVariable Integer id,
            RedirectAttributes flash,
            HttpSession session) {

        if (sinSesion(session)) {
            return "redirect:/";
        }

        try {

            productoService.changeActive(id);

            flash.addFlashAttribute(
                    "mensaje",
                    "Estado del producto actualizado correctamente."
            );

        } catch (IllegalArgumentException e) {

            flash.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/producto/listado";
    }

}
