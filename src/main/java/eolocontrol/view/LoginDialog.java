package eolocontrol.view;

import eolocontrol.dao.UsuarioDao;
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
    private final UsuarioDao usuarioDao;
    private final JTextField usuarioField = new JTextField("admin");
    private final JPasswordField passwordField = new JPasswordField("admin123");
    private Optional<Usuario> usuarioAutenticado = Optional.empty();

    public LoginDialog(JFrame parent, UsuarioDao usuarioDao) {
        super(parent, "Acceso a EoloControl", true);
        this.usuarioDao = usuarioDao;
        configurar();
    }

    public Optional<Usuario> mostrar() {
        setLocationRelativeTo(getParent());
        setVisible(true);
        return usuarioAutenticado;
    }

    private void configurar() {
        JLabel titulo = new JLabel("EoloControl");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(23, 88, 83));

        JLabel subtitulo = new JLabel("Monitoreo de centrales y turbinas eolicas");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(84, 100, 111));

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 2));
        encabezado.setBackground(Color.WHITE);
        encabezado.add(titulo);
        encabezado.add(subtitulo);

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 0));
        form.add(new JLabel("Usuario"));
        form.add(usuarioField);
        form.add(new JLabel("Contrasenia"));
        form.add(passwordField);

        JButton ingresarButton = new JButton("Ingresar");
        JButton cancelarButton = new JButton("Cancelar");
        ingresarButton.setBackground(new Color(28, 115, 107));
        ingresarButton.setForeground(Color.WHITE);
        ingresarButton.setFocusPainted(false);
        ingresarButton.addActionListener(event -> autenticar());
        cancelarButton.addActionListener(event -> dispose());

        JPanel acciones = new JPanel();
        acciones.setBackground(Color.WHITE);
        acciones.add(ingresarButton);
        acciones.add(cancelarButton);

        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        contenido.setBackground(Color.WHITE);
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
            usuarioAutenticado = usuarioDao.autenticar(usuario, contrasenia);
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

