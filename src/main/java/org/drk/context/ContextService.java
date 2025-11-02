package org.drk.context;

import org.drk.data.CsvDataService;
import org.drk.data.DataService;
import org.drk.data.Pelicula;
import org.drk.user.CsvUserService;
import org.drk.user.UserService;
import org.drk.user.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Servicio encargado de mantener datos precargados (usuarios y películas)
 * para evitar relecturas innecesarias de los archivos CSV.
 *
 * También proporciona acceso centralizado a los servicios de datos.
 */
public class ContextService {

    private static ContextService instance;
    private static HashMap<String, Object> data = new HashMap<>();

    private ContextService() {}

    public static ContextService getInstance() {
        if (instance == null) {
            instance = new ContextService();
        }
        return instance;
    }

    public void addItem(String key, Object o) {
        data.put(key, o);
    }
    public Optional<Object> getItem(String key) {
        return Optional.ofNullable(data.get(key));
    }
}
