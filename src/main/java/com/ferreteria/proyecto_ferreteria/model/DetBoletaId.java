package com.ferreteria.proyecto_ferreteria.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetBoletaId implements Serializable {
	
	@ManyToOne
	@JoinColumn(name = "num_boleta")
	private Boleta boleta;
	
	@ManyToOne
	@JoinColumn(name = "id_producto")
	private Producto producto;
}
