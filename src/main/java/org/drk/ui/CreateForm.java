package org.drk.ui;

import org.drk.context.AppContext;
import org.drk.context.ContextService;
import org.drk.data.Pelicula;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.UUID;

/**
 * Formulario para crear una nueva película.
 * Al guardar, se añade al CSV y se actualiza el contexto.
 */
public class CreateForm extends JDialog {

    private JTextField tituloField;
    private JTextField anioField;
    private JTextField directorField;
    private JTextField generoField;
    private JTextField imagenField;
    private JTextArea descripcionArea;
    private final ContextService contextService;

    public CreateForm(Frame parent, ContextService contextService) {
        super(parent, "Añadir Película", true);
        this.contextService = contextService;
        initUI();
    }

    private void initUI() {
        setSize(500, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Título:"));
        tituloField = new JTextField();
        form.add(tituloField);

        form.add(new JLabel("Año:"));
        anioField = new JTextField();
        form.add(anioField);

        form.add(new JLabel("Director:"));
        directorField = new JTextField();
        form.add(directorField);

        form.add(new JLabel("Género:"));
        generoField = new JTextField();
        form.add(generoField);

        form.add(new JLabel("URL Imagen:"));
        imagenField = new JTextField();
        form.add(imagenField);

        form.add(new JLabel("Descripción:"));
        descripcionArea = new JTextArea(4, 20);
        JScrollPane scroll = new JScrollPane(descripcionArea);
        add(form, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        JPanel botones = new JPanel();
        JButton guardarBtn = new JButton("Guardar");
        JButton cancelarBtn = new JButton("Cancelar");
        botones.add(guardarBtn);
        botones.add(cancelarBtn);
        add(botones, BorderLayout.SOUTH);

        guardarBtn.addActionListener(this::guardarPelicula);
        cancelarBtn.addActionListener(e -> dispose());
    }

    private void guardarPelicula(ActionEvent e) {
        try {
            AppContext appCtx = contextService.getAppContext();
            var usuario = appCtx.getUsuarioActual();

            String id = UUID.randomUUID().toString();
            String titulo = tituloField.getText().trim();
            int anio = Integer.parseInt(anioField.getText().trim());
            String director = directorField.getText().trim();
            String genero = generoField.getText().trim();
            String imagen = imagenField.getText().trim();
            String descripcion = descripcionArea.getText().trim();

            Pelicula nueva = new Pelicula(
                    id, titulo, anio, director, descripcion, genero, imagen, usuario.getId()
            );

            contextService.agregarPelicula(nueva);
            appCtx.getPeliculasUsuario().add(nueva);
            JOptionPane.showMessageDialog(this, "Película añadida correctamente");
            dispose();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la película: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El año debe ser un número válido");
        }
    }
}
