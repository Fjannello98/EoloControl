package eolocontrol.view;

import eolocontrol.controller.LoginController;
import eolocontrol.model.Usuario;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Optional;

public class LoginDialog extends JDialog {
    private static final Color COLOR_PRIMARIO = new Color(34, 91, 112);

    private final LoginController loginController;
    private final JTextField usuarioField = new JTextField("admin");
    private final JPasswordField passwordField = new JPasswordField("admin123");
    private Optional<Usuario> usuarioAutenticado = Optional.empty();

    public LoginDialog(JFrame parent, LoginController loginController) {
        super(parent, "Acceso a EoloControl", true);
        this.loginController = loginController;
        configurar();
    }

    public Optional<Usuario> mostrar() {
        setLocationRelativeTo(getParent());
        setVisible(true);
        return usuarioAutenticado;
    }

    private void configurar() {
        JLabel titulo = new JLabel("EoloControl");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));

        JLabel subtitulo = new JLabel("Monitoreo de centrales y turbinas eolicas");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 2));
        encabezado.add(titulo);
        encabezado.add(subtitulo);

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 0));
        form.add(new JLabel("Usuario"));
        form.add(usuarioField);
        form.add(new JLabel("Contrasenia"));
        form.add(passwordField);

        JButton ingresarButton = new JButton("Ingresar");
        JButton cancelarButton = new JButton("Cancelar");
        ingresarButton.putClientProperty("JButton.buttonType", "roundRect");
        ingresarButton.putClientProperty("FlatLaf.style", "background: #225B70; foreground: #FFFFFF; hoverBackground: #2E6E86; pressedBackground: #19495B;");
        ingresarButton.setFocusPainted(false);
        ingresarButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        ingresarButton.addActionListener(event -> autenticar());
        cancelarButton.putClientProperty("JButton.buttonType", "roundRect");
        cancelarButton.putClientProperty("FlatLaf.style", "focusColor: #4F93A8;");
        cancelarButton.setFocusPainted(false);
        cancelarButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        cancelarButton.addActionListener(event -> dispose());

        JPanel acciones = new JPanel();
        acciones.add(ingresarButton);
        acciones.add(cancelarButton);

        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(22, 24, 20, 24));
        contenido.add(encabezado, BorderLayout.NORTH);
        contenido.add(form, BorderLayout.CENTER);
        contenido.add(acciones, BorderLayout.SOUTH);

        setContentPane(contenido);
        getRootPane().setDefaultButton(ingresarButton);
        pack();
        setResizable(false);
    }

    private void autenticar() {
        String usuario = usuarioField.getText().trim();
        String contrasenia = new String(passwordField.getPassword());
        try {
            usuarioAutenticado = loginController.autenticar(usuario, contrasenia);
            if (usuarioAutenticado.isPresent()) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contrasenia incorrectos.", "Acceso", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de conexion", JOptionPane.ERROR_MESSAGE);
        }
    }
}

