package com.ferreteria.proyecto_ferreteria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ferreteria.proyecto_ferreteria.model.Boleta;

public interface BoletaRepository extends JpaRepository<Boleta, Integer> {
	
	@Query("""
			select distinct b from Boleta b
			left join fetch b.usuario
			left join fetch b.cliente
			where
				(b.usuario.idUsuario = :idUsuario or :idUsuario is null)
			order by
				b.numBoleta desc
			""")
	List<Boleta> findByFilters(@Param("idUsuario") Integer idUsuario);
}
