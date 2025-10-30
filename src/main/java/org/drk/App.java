package org.drk;

import org.drk.context.ContextService;
import org.drk.data.CsvDataService;
import org.drk.user.CsvUserService;
import org.drk.ui.Login;

import javax.swing.*;
import java.nio.file.Paths;

/**
 * Punto de entrada principal de la aplicación.
 */
public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Configuración de rutas
                var peliculasPath = Paths.get("peliculas.csv");
                var usuariosPath = Paths.get("usuarios.csv");

                // Servicios
                var dataService = new CsvDataService(peliculasPath);
                var userService = new FileUserService(usuariosPath);

                var context = ContextService.getInstance();
                context.setPeliculas(dataService.leerPeliculas());
                context.setUsuarios(userService.leerUsuarios());

                // Lanzar login
                new Login(context).setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error al iniciar la aplicación: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
