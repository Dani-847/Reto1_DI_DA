package org.drk.context;

import org.drk.data.Pelicula;
import org.drk.user.User;

/**
 * Clase singleton que mantiene el contexto de ejecución de la aplicación.
 * Permite acceder al usuario actualmente autenticado y a la película seleccionada.
 */
public class AppContext {

    private static AppContext instance;

    private User usuarioActual;
    private Pelicula peliculaSeleccionada;

    private AppContext() {}

    public static synchronized AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    public User getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(User usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public Pelicula getPeliculaSeleccionada() {
        return peliculaSeleccionada;
    }

    public void setPeliculaSeleccionada(Pelicula peliculaSeleccionada) {
        this.peliculaSeleccionada = peliculaSeleccionada;
    }

    public void limpiarContexto() {
        this.usuarioActual = null;
        this.peliculaSeleccionada = null;
    }
}
