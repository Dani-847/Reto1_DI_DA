package org.drk.ui;

import org.drk.context.AppContext;
import org.drk.context.ContextService;
import org.drk.user.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Pantalla inicial de autenticación.
 * Permite iniciar sesión con los datos del archivo usuarios.csv.
 */
public class Login extends JFrame {

    private final ContextService contextService;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JButton loginButton;

    public Login(ContextService contextService) {
        this.contextService = contextService;
        initUI();
    }

    private void initUI() {
        setTitle("Gestor de Películas - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 240);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Iniciar sesión", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.add(new JLabel("Email:"));
        emailField = new JTextField();
        form.add(emailField);

        form.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        form.add(passwordField);

        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        form.add(new JLabel());
        form.add(messageLabel);

        panel.add(form, BorderLayout.CENTER);

        loginButton = new JButton("Entrar");
        loginButton.addActionListener(this::onLogin);
        panel.add(loginButton, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private void onLogin(ActionEvent e) {
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (email.isEmpty() || pass.isEmpty()) {
            messageLabel.setText("Por favor, completa ambos campos");
            return;
        }

        try {
            Usuario u = contextService.autenticar(email, pass);
            if (u != null) {
                AppContext appCtx = contextService.getAppContext();
                appCtx.setUsuarioActual(u);
                appCtx.setPeliculasUsuario(contextService.getPeliculasPorUsuario(u));

                new Main(contextService).setVisible(true);
                dispose();
            } else {
                messageLabel.setText("Credenciales incorrectas");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            messageLabel.setText("Error al acceder a los datos");
        }
    }
}
