package com.ferreteria.proyecto_ferreteria.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductoSeleccionado {
	private Integer idProducto;
	private String descripcion;
	private Integer cantidad;
	private BigDecimal precio;
	
	public BigDecimal getSubtotal() {
		if (precio == null || cantidad == null) return BigDecimal.ZERO;
		return precio.multiply(BigDecimal.valueOf(cantidad));
	}
}
