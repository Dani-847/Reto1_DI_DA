package org.drk.user;

import org.drk.data.Pelicula;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el manejo de usuarios.
 * Define las operaciones esenciales de autenticación y lectura de datos.
 */
public interface UserService {

    Optional<User> validate(ArrayList<User> usuarios, String correo, String contraseña);

    public List<User> findAll();
    Optional<User> save(User user);

}
