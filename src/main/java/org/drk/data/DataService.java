package org.drk.data;

import java.io.IOException;
import java.util.List;

/**
 * Interfaz base para los servicios de acceso a datos (DAO).
 * Define las operaciones de lectura y escritura de películas.
 */
public interface DataService {

    /**
     * Devuelve todas las películas del archivo CSV.
     */
    List<Pelicula> leerPeliculas() throws IOException;

    /**
     * Devuelve las películas asociadas a un usuario concreto.
     */
    List<Pelicula> leerPeliculasPorUsuario(int usuarioId) throws IOException;

    /**
     * Añade una nueva película al CSV.
     */
    void agregarPelicula(Pelicula pelicula) throws IOException;

    /**
     * Elimina una película por su ID.
     */
    void eliminarPelicula(int id) throws IOException;
}
