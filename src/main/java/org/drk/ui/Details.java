package org.drk.ui;

import org.drk.context.ContextService;
import org.drk.data.Pelicula;

import javax.swing.*;
import java.awt.event.*;

public class Details extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel labelId;
    private JLabel labelTitulo;
    private JLabel labelAño;
    private JLabel labelDirector;
    private JLabel labelDescripcion;     // reutilizado como Director
    private JLabel labelImagen;           // reutilizado como Género
    private JLabel labelGenero; // usuarioId
    private JLabel labelOwner;

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
            labelTitulo.setText(pelicula.getTitulo());
            labelAño.setText(String.valueOf(pelicula.getAño()));
            labelDirector.setText(pelicula.getDirector());
            labelDescripcion.setText(pelicula.getDescripcion());
            labelImagen.setText(pelicula.getImagenUrl());
            labelGenero.setText(pelicula.getGenero());
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
