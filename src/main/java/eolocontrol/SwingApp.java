package eolocontrol;

import com.formdev.flatlaf.FlatLightLaf;

import eolocontrol.controller.ApplicationController;
import eolocontrol.controller.EoloControlController;
import eolocontrol.controller.LoginController;
import eolocontrol.dao.AlertaDao;
import eolocontrol.dao.CentralDao;
import eolocontrol.dao.ReporteDao;
import eolocontrol.dao.TelemetriaDao;
import eolocontrol.dao.TurbinaDao;
import eolocontrol.dao.UsuarioDao;
import eolocontrol.model.Usuario;
import eolocontrol.service.AlertaService;
import eolocontrol.service.InventarioService;
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
            LoginController loginController = new LoginController(usuarioDao);
            Optional<Usuario> usuario = new LoginDialog(null, loginController).mostrar();
            if (usuario.isEmpty()) {
                return;
            }

            CentralDao centralDao = new CentralDao();
            TurbinaDao turbinaDao = new TurbinaDao();
            TelemetriaDao telemetriaDao = new TelemetriaDao();
            AlertaDao alertaDao = new AlertaDao();
            ReporteDao reporteDao = new ReporteDao();
            AlertaService alertaService = new AlertaService(alertaDao);
            InventarioService inventarioService = new InventarioService();
            ApplicationController controller = new EoloControlController(
                    centralDao,
                    turbinaDao,
                    telemetriaDao,
                    alertaDao,
                    reporteDao,
                    alertaService,
                    inventarioService);

            MainFrame frame = new MainFrame(usuario.get(), controller);
            frame.setVisible(true);
        });
    }
}

