package org.drk.context;

import org.drk.data.Pelicula;
import org.drk.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de mantener en memoria las listas de películas y usuarios
 * para evitar relecturas innecesarias desde disco.
 *
 * Las listas no se modifican directamente para evitar inconsistencias con los CSV.
 * En su lugar, se usan copias temporales.
 */
public class ContextService {

    private static ContextService instance;

    private List<Pelicula> cachePeliculas = new ArrayList<>();
    private List<User> cacheUsuarios = new ArrayList<>();

    private ContextService() {}

    public static synchronized ContextService getInstance() {
        if (instance == null) {
            instance = new ContextService();
        }
        return instance;
    }

    public void setPeliculas(List<Pelicula> peliculas) {
        cachePeliculas.clear();
        if (peliculas != null) cachePeliculas.addAll(peliculas);
    }

    public void setUsuarios(List<User> usuarios) {
        cacheUsuarios.clear();
        if (usuarios != null) cacheUsuarios.addAll(usuarios);
    }

    public List<Pelicula> getPeliculas() {
        return new ArrayList<>(cachePeliculas); // copia para no alterar el original
    }

    public List<User> getUsuarios() {
        return new ArrayList<>(cacheUsuarios);
    }

    public User buscarUsuarioPorId(int id) {
        return cacheUsuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Pelicula> getPeliculasPorUsuario(int usuarioId) {
        List<Pelicula> resultado = new ArrayList<>();
        for (Pelicula p : cachePeliculas) {
            if (p.getUsuarioId() == usuarioId) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}
