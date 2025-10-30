package org.drk.ui;

import org.drk.context.AppContext;
import org.drk.context.ContextService;
import org.drk.data.Pelicula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

/**
 * Vista principal que muestra las películas del usuario autenticado.
 */
public class ListView extends JFrame {

    private final ContextService contextService;
    private final AppContext appContext;

    private JTable tablaPeliculas;
    private DefaultTableModel modeloTabla;

    public Main(ContextService contextService) {
        this.contextService = contextService;
        this.appContext = contextService.getAppContext();
        initUI();
        cargarPeliculas();
    }

    private void initUI() {
        setTitle("Mis Películas - " + appContext.getUsuarioActual().getEmail());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Título", "Año", "Director", "Género"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaPeliculas = new JTable(modeloTabla);
        tablaPeliculas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tablaPeliculas), BorderLayout.CENTER);

        // Botonera inferior
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDetalles = new JButton("Detalles");
        JButton btnAñadir = new JButton("Añadir");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnSalir = new JButton("Cerrar sesión");

        botones.add(btnDetalles);
        botones.add(btnAñadir);
        botones.add(btnEliminar);
        botones.add(btnSalir);
        panel.add(botones, BorderLayout.SOUTH);

        // Listeners
        btnDetalles.addActionListener(this::verDetalles);
        btnAñadir.addActionListener(this::abrirCrear);
        btnEliminar.addActionListener(this::eliminarPelicula);
        btnSalir.addActionListener(e -> cerrarSesion());

        setContentPane(panel);
    }

    private void cargarPeliculas() {
        modeloTabla.setRowCount(0);
        List<Pelicula> peliculas = appContext.getPeliculasUsuario();
        for (Pelicula p : peliculas) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getTitulo(),
                    p.getAnio(),
                    p.getDirector(),
                    p.getGenero()
            });
        }
    }

    private void verDetalles(ActionEvent e) {
        int row = tablaPeliculas.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una película primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = modeloTabla.getValueAt(row, 0).toString();
        Pelicula p = appContext.buscarPorId(id);
        if (p != null) {
            new Details(this, p).setVisible(true);
        }
    }

    private void abrirCrear(ActionEvent e) {
        new CreateForm(this, contextService).setVisible(true);
    }

    private void eliminarPelicula(ActionEvent e) {
        int row = tablaPeliculas.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una película para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = modeloTabla.getValueAt(row, 0).toString();
        Pelicula p = appContext.buscarPorId(id);

        if (p != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar '" + p.getTitulo() + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    contextService.eliminarPelicula(p);
                    appContext.getPeliculasUsuario().remove(p);
                    cargarPeliculas();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al eliminar la película.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void cerrarSesion() {
        appContext.cerrarSesion();
        new Login(contextService).setVisible(true);
        dispose();
    }
}
