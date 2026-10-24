package com.ferreteria.proyecto_ferreteria.model;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_cliente")
@Getter
@Setter
@DynamicInsert
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cliente")
	private Integer idCliente;

	@Column(name = "nombres")
	private String nombres;

	@Column(name = "apellidos")
	private String apellidos;

	@Column(name = "dni")
	private String dni;

	@Column(name = "celular")
	private String celular;
	
	@Column(name = "activo")
	private Boolean activo;
	
	public String getActivoDescripcion() {
		return activo ? "Activo" : "Inactivo";
	}

	public String getFullName() {
		return String.format("%s %s", nombres, apellidos);
	}
}
