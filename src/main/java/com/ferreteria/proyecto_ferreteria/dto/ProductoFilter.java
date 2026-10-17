
package com.ferreteria.proyecto_ferreteria.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoFilter {

    private String descripcion;

    private Integer idCategoria;

    private Boolean activo;

}
