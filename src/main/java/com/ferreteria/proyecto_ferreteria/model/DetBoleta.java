package com.ferreteria.proyecto_ferreteria.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_det_boleta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetBoleta {
	
	@EmbeddedId
	private DetBoletaId id;
	
	@Column(name = "cantidad")
	private Integer cantidad;
	
	@Column(name = "precio_venta")
	private BigDecimal precioVenta;
}
