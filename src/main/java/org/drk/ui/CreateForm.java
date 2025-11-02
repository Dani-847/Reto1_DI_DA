package org.drk.ui;

import org.drk.context.ContextService;
import org.drk.data.DataService;
import org.drk.data.Pelicula;
import org.drk.user.User;

import javax.swing.*;
import javax.swing.SpinnerNumberModel;
import java.awt.event.*;

public class CreateForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textTitulo;
    private JComboBox comboBox1;            // Género
    private JSpinner spinnerAño;
    private JSpinner spinnerPropietario;    // Id usuario (solo lectura)
    private JTextField textLinkUrl;         // Imagen/Link
    private JTextArea textAreaDescripcion;
    private JTextField textDirector;

    private final DataService dataService;

    public CreateForm(DataService ds) {
        this.dataService = ds;

        setTitle("Nueva película");
        setContentPane(contentPane);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setResizable(false);
        getRootPane().setDefaultButton(buttonOK);

        // Año
        spinnerAño.setModel(new SpinnerNumberModel(1990, 1900, 2100, 1));

        // Propietario: fijar con el usuario autenticado y deshabilitar edición
        User user = (User) ContextService.getInstance().getItem("usuarioActivo").orElse(null);
        int uid = user != null ? user.getId() : 0;
        spinnerPropietario.setModel(new SpinnerNumberModel(uid, 0, Integer.MAX_VALUE, 1));
        spinnerPropietario.setEnabled(false);
        spinnerPropietario.setToolTipText("Se asigna automáticamente al usuario autenticado");

        // Listeners
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { onCancel(); }
        });
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setLocationRelativeTo(null);
    }

    private void onOK() {
        User user = (User) ContextService.getInstance().getItem("usuarioActivo").orElse(null);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Inicie sesión primero", "", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String titulo = textTitulo.getText() != null ? textTitulo.getText().trim() : "";
        String director = textDirector.getText() != null ? textDirector.getText().trim() : "";
        String descripcion = textAreaDescripcion.getText() != null ? textAreaDescripcion.getText().trim() : "";
        Object generoObj = comboBox1.getSelectedItem();
        String genero = generoObj != null ? generoObj.toString().trim() : "";
        String link = textLinkUrl.getText() != null ? textLinkUrl.getText().trim() : "";
        int anio = (Integer) spinnerAño.getValue();

        // Validaciones mínimas
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título es obligatorio", "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (director.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El director es obligatorio", "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (genero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El género es obligatorio", "", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(titulo);
        pelicula.setAño(anio);
        pelicula.setDirector(director);
        pelicula.setDescripcion(descripcion.isEmpty() ? "Sin descripción" : descripcion);
        pelicula.setGenero(genero);
        pelicula.setImagenUrl(link.isEmpty() ? "sin-imagen" : link);
        pelicula.setUsuarioId(user.getId()); // Forzar propietario al usuario autenticado

        if (dataService.save(pelicula).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error al guardar", "", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Película guardada");
            dispose();
        }
    }

    private void onCancel() { dispose(); }
}
