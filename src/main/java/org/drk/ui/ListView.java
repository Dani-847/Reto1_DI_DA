package org.drk.ui;

import org.drk.context.ContextService;
import org.drk.data.Pelicula;
import org.drk.data.DataService;
import org.drk.user.User;
import org.drk.user.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table1.getSelectedRow() >= 0) {
                Pelicula pelicula = peliculas.get(table1.getSelectedRow());
                ContextService.getInstance().addItem("peliculaSeleccionada", pelicula);
                new Details(this).setVisible(true);
            }
        });
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
    }

    private JMenuBar PrepareMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu jMenuInicio = new JMenu("Inicio");
        JMenuItem menuItemLogin = new JMenuItem("Login");
        menuItemAñadir = new JMenuItem("Añadir");
        menuItemAñadir.setEnabled(false);
        JMenuItem menuItemSalir = new JMenuItem("Salir");

        menuBar.add(jMenuInicio);
        jMenuInicio.add(menuItemLogin);
        jMenuInicio.addSeparator();
        jMenuInicio.add(menuItemAñadir);
        jMenuInicio.addSeparator();
        jMenuInicio.add(menuItemSalir);

        menuItemLogin.addActionListener(e -> {
            new Login(this, userservice).setVisible(true);
            ContextService.getInstance().getItem("usuarioActivo").ifPresent(_ -> {
                menuItemAñadir.setEnabled(true);
                loadDataTable();
            });
        });

        menuItemSalir.addActionListener(e -> System.exit(0));
        menuItemAñadir.addActionListener(e -> {
            new CreateForm(dataservice).setVisible(true);
            loadDataTable();
        });
        return menuBar;
    }

    public void start() { setVisible(true); }
}
