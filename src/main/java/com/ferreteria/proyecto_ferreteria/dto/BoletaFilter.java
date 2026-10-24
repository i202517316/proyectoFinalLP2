package com.ferreteria.proyecto_ferreteria.dto;

public record BoletaFilter(
	Integer idUsuario
) {
	public BoletaFilter withIdUsuario(Integer idUsuario) {
		return new BoletaFilter(idUsuario);
	}
}
