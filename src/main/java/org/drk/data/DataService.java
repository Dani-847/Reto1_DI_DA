package org.drk.data;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz encargada de gestionar la persistencia de los datos
 */
public interface DataService {
    List<Pelicula> findAll();
    Optional<Pelicula> save(Pelicula pelicula);

    // Nuevo: eliminar por id
    boolean deleteById(int id);
}
