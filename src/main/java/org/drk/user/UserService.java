package org.drk.user;

import java.io.IOException;
import java.util.List;

/**
 * Interfaz para el manejo de usuarios.
 * Define las operaciones esenciales de autenticación y lectura de datos.
 */
public interface UserService {

    /**
     * Devuelve todos los usuarios registrados en el archivo CSV.
     */
    List<User> leerUsuarios() throws IOException;

    /**
     * Busca un usuario por email y contraseña (autenticación).
     */
    User autenticar(String email, String password) throws IOException;

    /**
     * Busca un usuario por su ID.
     */
    User buscarPorId(int id) throws IOException;

    /**
     * Registra un nuevo usuario en el CSV.
     */
    void registrarUsuario(User usuario) throws IOException;
}
