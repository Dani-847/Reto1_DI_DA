package org.drk.ui;

import org.drk.context.ContextService;
import org.drk.user.UserService;
import org.drk.user.User;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Login extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtUser;
    private JPasswordField txtPassword;

    private final UserService userService;
    private final JFrame parent;

    public Login(JFrame parent, UserService us) {
        this.parent = parent;
        this.userService = us;

        setContentPane(contentPane);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setResizable(false);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(parent);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { onCancel(); }
        });

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String correo = txtUser.getText();
        String contraseña = new String(txtPassword.getPassword());
        ArrayList<User> usuarios = new ArrayList<>(userService.findAll());

        userService.validate(usuarios, correo, contraseña).ifPresentOrElse(
                (User user) -> {
                    ContextService.getInstance().addItem("usuarioActivo", user);
                    dispose();
                },
                () -> JOptionPane.showMessageDialog(parent, "Usuario no existe"));
    }

    private void onCancel() { dispose(); }
}
