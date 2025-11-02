package org.drk.ui;

import org.drk.context.ContextService;
import org.drk.data.Pelicula;

import javax.swing.*;
import java.awt.event.*;

public class Details extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel labelId;
    private JLabel labelNombre;
    private JLabel labelDescripcion;
    private JLabel labelAño;
    private JLabel LabelImagen;
    private JLabel labelPlataforma;     // reutilizado como Director
    private JLabel labelTipo;           // reutilizado como Género
    private JLabel labelDesarrolladora; // no aplica, se deja "-"
    private JLabel labelOwner;          // usuarioId

    public Details(JFrame parent) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        Pelicula pelicula = (Pelicula) ContextService.getInstance()
                .getItem("peliculaSeleccionada").orElse(null);

        if (pelicula != null) {
            setTitle(pelicula.getTitulo());
            labelId.setText(String.valueOf(pelicula.getId()));
            labelNombre.setText(pelicula.getTitulo());
            labelDescripcion.setText(pelicula.getDescripcion());
            labelAño.setText(String.valueOf(pelicula.getAño()));
            LabelImagen.setText(pelicula.getImagenUrl());
            labelPlataforma.setText(pelicula.getDirector());
            labelTipo.setText(pelicula.getGenero());
            labelDesarrolladora.setText("-");
            labelOwner.setText(String.valueOf(pelicula.getUsuarioId()));
        }

        buttonOK.addActionListener(e -> onOK());

        contentPane.registerKeyboardAction(e -> onOK(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
    }

    private void onOK() { dispose(); }
}
