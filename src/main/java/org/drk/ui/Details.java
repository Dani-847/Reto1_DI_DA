package org.drk.ui;

import org.drk.data.Pelicula;

import javax.swing.*;
import java.awt.*;

/**
 * Muestra los detalles completos de una película seleccionada.
 */
public class Details extends JDialog {

    private final Pelicula pelicula;

    public Details(Frame parent, Pelicula pelicula) {
        super(parent, "Detalles de Película", true);
        this.pelicula = pelicula;
        initUI();
    }

    private void initUI() {
        setSize(500, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        JPanel info = new JPanel(new GridLayout(6, 1, 5, 5));

        info.add(new JLabel("Título: " + pelicula.getTitulo()));
        info.add(new JLabel("Año: " + pelicula.getAnio()));
        info.add(new JLabel("Director: " + pelicula.getDirector()));
        info.add(new JLabel("Género: " + pelicula.getGenero()));

        JTextArea descripcion = new JTextArea(pelicula.getDescripcion());
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setEditable(false);

        JScrollPane scroll = new JScrollPane(descripcion);
        info.add(scroll);

        JLabel imagen = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(pelicula.getImagenUrl()));
            Image img = icon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
            imagen.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imagen.setText("Imagen no disponible");
        }

        add(info, BorderLayout.CENTER);
        add(imagen, BorderLayout.NORTH);

        JButton cerrarBtn = new JButton("Cerrar");
        cerrarBtn.addActionListener(e -> dispose());
        add(cerrarBtn, BorderLayout.SOUTH);
    }
}
