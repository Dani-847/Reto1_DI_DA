package org.drk.context;

import lombok.Setter;
import org.drk.data.Pelicula;
import org.drk.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que mantiene el estado actual de la aplicación en memoria.
 * Contiene el usuario autenticado y la lista de películas visibles en sesión.
 *
 * Inspirada en el modelo de "Juegoteca", pero adaptada a soporte multiusuario.
 */
public class AppContext {

    public static Pelicula peliculaSeleccionada = null;
    public static User currentUser = null;

}
