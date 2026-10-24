package com.ferreteria.proyecto_ferreteria.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_cab_boleta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Boleta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "num_boleta")
	private Integer numBoleta;
	
	@Column(name = "fecha_registro")
	private LocalDateTime fechaRegistro;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;
	
	@OneToMany(mappedBy = "id.boleta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<DetBoleta> lstDetBoleta;
	
	public BigDecimal getTotal() {
		if (lstDetBoleta == null) return BigDecimal.ZERO;
		return lstDetBoleta.stream().map(d -> {
			var cantidad = BigDecimal.valueOf(d.getCantidad());
			return d.getPrecioVenta().multiply(cantidad);
		}).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
