package org.drk.ui;

import org.drk.context.ContextService;
import org.drk.data.Pelicula;
import org.drk.data.DataService;
import org.drk.user.User;
import org.drk.user.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class ListView extends JFrame {
    private JTable table1;
    private JPanel panel1;

    private final DataService dataservice;
    private final UserService userservice;

    private ArrayList<Pelicula> peliculas = new ArrayList<>();
    private JMenuItem menuItemAñadir;
    private JMenuItem menuItemEliminar;

    public ListView(DataService ds, UserService us) {
        dataservice = ds;
        userservice = us;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Películas");
        setResizable(false);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setContentPane(panel1);

        JMenuBar menuBar = PrepareMenuBar();
        panel1.add(menuBar, BorderLayout.NORTH);

        var modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Título");
        modelo.addColumn("Descripción");
        table1.setModel(modelo);

        loadDataTable();
        updateAddEnabled();

        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table1.getSelectedRow() >= 0) {
                Pelicula pelicula = peliculas.get(table1.getSelectedRow());
                ContextService.getInstance().addItem("peliculaSeleccionada", pelicula);
                updateEliminarEnabled();
                new Details(this).setVisible(true);
            }
        });

        var actionEliminar = new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { performEliminar(); }
        };
        table1.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "eliminar");
        table1.getActionMap().put("eliminar", actionEliminar);
    }

    private void loadDataTable() {
        DefaultTableModel modelo = (DefaultTableModel) table1.getModel();
        modelo.setRowCount(0);

        User usuario = (User) ContextService.getInstance()
                .getItem("usuarioActivo").orElse(null);
        Integer uid = usuario != null ? usuario.getId() : null;

        peliculas = dataservice.findAll().stream()
                .filter(p -> uid == null || Objects.equals(p.getUsuarioId(), uid))
                .collect(Collectors.toCollection(ArrayList::new));

        peliculas.forEach(p -> {
            var fila = new Object[]{ p.getId(), p.getTitulo(), p.getDescripcion() };
            modelo.addRow(fila);
        });

        updateEliminarEnabled();
    }

    private JMenuBar PrepareMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu jMenuInicio = new JMenu("Acciones");
        JMenuItem menuItemLogin = new JMenuItem("Cambiar Usuario");
        menuItemAñadir = new JMenuItem("Añadir");
        menuItemEliminar = new JMenuItem("Eliminar");
        menuItemAñadir.setEnabled(false);
        menuItemEliminar.setEnabled(false);
        JMenuItem menuItemSalir = new JMenuItem("Salir");

        menuBar.add(jMenuInicio);
        jMenuInicio.add(menuItemLogin);
        jMenuInicio.addSeparator();
        jMenuInicio.add(menuItemAñadir);
        jMenuInicio.add(menuItemEliminar);
        jMenuInicio.addSeparator();
        jMenuInicio.add(menuItemSalir);

        menuItemLogin.addActionListener(e -> {
            new Login(this, userservice).setVisible(true);
            updateAddEnabled();
            loadDataTable();
        });

        menuItemSalir.addActionListener(e -> System.exit(0));
        menuItemAñadir.addActionListener(e -> {
            new CreateForm(dataservice).setVisible(true);
            loadDataTable();
        });
        menuItemEliminar.addActionListener(e -> performEliminar());
        return menuBar;
    }

    private void performEliminar() {
        int row = table1.getSelectedRow();
        if (row < 0) return;

        User user = (User) ContextService.getInstance().getItem("usuarioActivo").orElse(null);
        if (user == null) return;

        Pelicula pelicula = peliculas.get(row);
        if (!Objects.equals(pelicula.getUsuarioId(), user.getId())) {
            JOptionPane.showMessageDialog(this, "Solo puede eliminar sus propias películas", "", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar \"" + pelicula.getTitulo() + "\"?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            if (dataservice.deleteById(pelicula.getId())) {
                loadDataTable();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar", "", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void updateEliminarEnabled() {
        User user = (User) ContextService.getInstance().getItem("usuarioActivo").orElse(null);
        boolean enabled = false;
        int row = table1.getSelectedRow();
        if (user != null && row >= 0 && row < peliculas.size()) {
            Pelicula p = peliculas.get(row);
            enabled = Objects.equals(p.getUsuarioId(), user.getId());
        }
        if (menuItemEliminar != null) menuItemEliminar.setEnabled(enabled);
    }

    private void updateAddEnabled() {
        boolean loggedIn = ContextService.getInstance().getItem("usuarioActivo").isPresent();
        if (menuItemAñadir != null) menuItemAñadir.setEnabled(loggedIn);
    }

    public void start() { setVisible(true); }
}
