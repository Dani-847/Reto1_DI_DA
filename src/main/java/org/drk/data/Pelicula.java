package org.drk.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Clase de modelo que representa una película en el sistema.
 */
@Data
public class Pelicula {
    private Integer id;
    private String titulo;
    private Integer año;
    private String director;
    private String descripcion;
    private String genero;
    private String imagenUrl;
    private Integer usuarioId;
}