
package com.ferreteria.proyecto_ferreteria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ferreteria.proyecto_ferreteria.model.Producto;

public interface ProductoRepository
        extends JpaRepository<Producto, Integer> {

    @Query("""
      SELECT p FROM Producto p
        WHERE (:descripcion IS NULL
               OR LOWER(p.descripcion) LIKE
               LOWER(CONCAT('%', :descripcion, '%')))
          AND (:idCategoria IS NULL
               OR p.categoria.idCategoria = :idCategoria)
          AND (:activo IS NULL
               OR p.activo = :activo)
        ORDER BY p.idProducto DESC
        """)
    List<Producto> buscarProductos(
            @Param("descripcion") String descripcion,
            @Param("idCategoria") Integer idCategoria,
            @Param("activo") Boolean activo
    );
    

}
