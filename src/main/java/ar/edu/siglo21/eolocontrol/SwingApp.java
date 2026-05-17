package ar.edu.siglo21.eolocontrol;

import ar.edu.siglo21.eolocontrol.dao.AlertaDao;
import ar.edu.siglo21.eolocontrol.dao.CentralDao;
import ar.edu.siglo21.eolocontrol.dao.ReporteDao;
import ar.edu.siglo21.eolocontrol.dao.TelemetriaDao;
import ar.edu.siglo21.eolocontrol.dao.TurbinaDao;
import ar.edu.siglo21.eolocontrol.dao.UsuarioDao;
import ar.edu.siglo21.eolocontrol.model.Usuario;
import ar.edu.siglo21.eolocontrol.service.AlertaService;
import ar.edu.siglo21.eolocontrol.view.LoginDialog;
import ar.edu.siglo21.eolocontrol.view.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.Optional;

public class SwingApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Swing usa su apariencia por defecto si no puede cargar la del sistema.
            }

            UsuarioDao usuarioDao = new UsuarioDao();
            Optional<Usuario> usuario = new LoginDialog(null, usuarioDao).mostrar();
            if (usuario.isEmpty()) {
                return;
            }

            CentralDao centralDao = new CentralDao();
            TurbinaDao turbinaDao = new TurbinaDao();
            TelemetriaDao telemetriaDao = new TelemetriaDao();
            AlertaDao alertaDao = new AlertaDao();
            ReporteDao reporteDao = new ReporteDao();
            AlertaService alertaService = new AlertaService(alertaDao);

            MainFrame frame = new MainFrame(
                    usuario.get(),
                    centralDao,
                    turbinaDao,
                    telemetriaDao,
                    alertaDao,
                    reporteDao,
                    alertaService);
            frame.setVisible(true);
        });
    }
}
