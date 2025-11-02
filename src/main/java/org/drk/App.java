package org.drk;

import org.drk.context.ContextService;
import org.drk.data.CsvDataService;
import org.drk.data.DataService;
import org.drk.ui.ListView;
import org.drk.ui.Login;
import org.drk.user.CsvUserService;
import org.drk.user.UserService;

import javax.swing.*;

/**
 * Punto de entrada principal de la aplicación.
 */
public class App {
    public static void main(String[] args) {
        DataService ds = new CsvDataService("peliculas.csv");
        UserService us = new CsvUserService("usuarios.csv");

        Login login = new Login(null, us);
        login.setVisible(true);

        if (ContextService.getInstance().getItem("usuarioActivo").isPresent()) {
            new ListView(ds, us).start();
        } else {
            System.exit(0);
        }
    }
}
