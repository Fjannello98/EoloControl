package eolocontrol;

import com.formdev.flatlaf.FlatLightLaf;

import eolocontrol.dao.AlertaDao;
import eolocontrol.dao.CentralDao;
import eolocontrol.dao.ReporteDao;
import eolocontrol.dao.TelemetriaDao;
import eolocontrol.dao.TurbinaDao;
import eolocontrol.dao.UsuarioDao;
import eolocontrol.model.Usuario;
import eolocontrol.service.AlertaService;
import eolocontrol.view.LoginDialog;
import eolocontrol.view.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Font;
import java.util.Optional;

public class SwingApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));
                FlatLightLaf.setup();
            } catch (Exception ignored) {
                // Swing usa su apariencia por defecto si no puede cargar FlatLaf.
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

