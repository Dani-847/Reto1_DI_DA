package org.drk.ui;

import org.drk.data.DataService;
import org.drk.data.Pelicula;

import javax.swing.*;
import java.awt.event.*;

public class CreateForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textTitulo;
    private JComboBox comboBox1;
    private JSpinner spinnerAño;

    private DataService dataService;

    public CreateForm(DataService ds) {

        dataService = ds;

        setContentPane(contentPane);
        setModalityType(ModalityType.APPLICATION_MODAL);
        getRootPane().setDefaultButton(buttonOK);
        setResizable(false);

        //( (DefaultComboBoxModel) comboBox1.getModel()).addAll(...);

        spinnerAño.setModel(new SpinnerNumberModel(1990, 1974, 2025, 1));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
    }

    private void onOK() {

        Pelicula pelicula = new Pelicula();

        pelicula.setTitulo(textTitulo.getText());
        pelicula.setAño((Integer) spinnerAño.getValue());
        pelicula.setDirector(comboBox1.getSelectedItem().toString());
        pelicula.setDescripcion("Des");
        pelicula.setGenero("Gen");
        pelicula.setImagenUrl("imagenUrl");
        pelicula.setUsuarioId(0);

        if(dataService.save(pelicula).isEmpty()){
            JOptionPane.showMessageDialog(this, "Error al guardar","",JOptionPane.WARNING_MESSAGE);
        } else dispose();

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
