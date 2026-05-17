package ar.edu.siglo21.eolocontrol.view;

import ar.edu.siglo21.eolocontrol.dao.UsuarioDao;
import ar.edu.siglo21.eolocontrol.model.Usuario;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
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
        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Usuario"));
        form.add(usuarioField);
        form.add(new JLabel("Contrasenia"));
        form.add(passwordField);

        JButton ingresarButton = new JButton("Ingresar");
        JButton cancelarButton = new JButton("Cancelar");
        ingresarButton.addActionListener(event -> autenticar());
        cancelarButton.addActionListener(event -> dispose());

        JPanel acciones = new JPanel();
        acciones.add(ingresarButton);
        acciones.add(cancelarButton);

        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        contenido.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
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
