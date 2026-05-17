package eolocontrol;

import eolocontrol.dao.AlertaDao;
import eolocontrol.dao.CentralDao;
import eolocontrol.dao.ReporteDao;
import eolocontrol.dao.TelemetriaDao;
import eolocontrol.dao.TurbinaDao;
import eolocontrol.dao.UsuarioDao;
import eolocontrol.model.CentralEolica;
import eolocontrol.model.RegistroTelemetria;
import eolocontrol.model.TurbinaEolica;
import eolocontrol.model.Usuario;
import eolocontrol.service.AlertaService;
import eolocontrol.view.ConsoleView;

public class App {
    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        UsuarioDao usuarioDao = new UsuarioDao();
        CentralDao centralDao = new CentralDao();
        TurbinaDao turbinaDao = new TurbinaDao();
        TelemetriaDao telemetriaDao = new TelemetriaDao();
        AlertaDao alertaDao = new AlertaDao();
        AlertaService alertaService = new AlertaService(alertaDao);
        ReporteDao reporteDao = new ReporteDao();

        view.titulo("EoloControl Java - Prototipo ECO Viento");

        Usuario usuario = autenticar(view, usuarioDao);
        if (usuario == null) {
            view.error("No se pudo autenticar el usuario.");
            return;
        }

        view.ok("Bienvenido " + usuario.nombreUsuario() + " (" + usuario.rol() + ")");

        boolean activo = true;
        while (activo) {
            try {
                switch (view.menu()) {
                    case 1 -> registrarCentral(view, centralDao);
                    case 2 -> listarCentrales(view, centralDao);
                    case 3 -> registrarTurbina(view, centralDao, turbinaDao);
                    case 4 -> listarTurbinas(view, turbinaDao);
                    case 5 -> registrarTelemetria(view, turbinaDao, telemetriaDao, alertaService);
                    case 6 -> reporteDao.reporteEnergiaPorTurbina().forEach(view::linea);
                    case 7 -> alertaDao.listarPendientes().forEach(view::linea);
                    case 0 -> activo = false;
                    default -> view.error("Opcion no valida.");
                }
            } catch (Exception ex) {
                view.error(ex.getMessage());
            }
        }

        view.ok("Sesion finalizada.");
    }

    private static Usuario autenticar(ConsoleView view, UsuarioDao usuarioDao) {
        String nombre = view.leerTexto("Usuario");
        String contrasenia = view.leerTexto("Contrasenia");
        return usuarioDao.autenticar(nombre, contrasenia).orElse(null);
    }

    private static void registrarCentral(ConsoleView view, CentralDao dao) {
        CentralEolica central = new CentralEolica(
                0,
                view.leerTexto("Nombre"),
                view.leerTexto("Ubicacion"),
                view.leerTexto("Provincia"));
        dao.crear(central);
        view.ok("Central registrada.");
    }

    private static void listarCentrales(ConsoleView view, CentralDao dao) {
        dao.listar().forEach(c -> view.linea(c.id() + " - " + c.nombre() + " - " + c.ubicacion() + ", " + c.provincia()));
    }

    private static void registrarTurbina(ConsoleView view, CentralDao centralDao, TurbinaDao turbinaDao) {
        listarCentrales(view, centralDao);
        TurbinaEolica turbina = new TurbinaEolica(
                0,
                view.leerEntero("ID central"),
                view.leerTexto("Codigo"),
                view.leerTexto("Modelo"),
                view.leerDecimal("Potencia maxima kW"),
                view.leerTexto("Estado (OPERATIVA/MANTENIMIENTO/FALLA)"));
        turbinaDao.crear(turbina);
        view.ok("Turbina registrada.");
    }

    private static void listarTurbinas(ConsoleView view, TurbinaDao dao) {
        dao.listar().forEach(t -> view.linea(t.id() + " - " + t.codigo() + " - " + t.modelo()
                + " - " + t.estado() + " - " + t.potenciaMaximaKw() + " kW"));
    }

    private static void registrarTelemetria(
            ConsoleView view,
            TurbinaDao turbinaDao,
            TelemetriaDao telemetriaDao,
            AlertaService alertaService) {
        listarTurbinas(view, turbinaDao);
        RegistroTelemetria registro = new RegistroTelemetria(
                0,
                view.leerEntero("ID turbina"),
                view.leerFechaHora("Fecha y hora (yyyy-MM-dd HH:mm)"),
                view.leerDecimal("Velocidad viento km/h"),
                view.leerTexto("Direccion viento"),
                view.leerDecimal("Energia generada MWh"));

        int idRegistro = telemetriaDao.crear(registro);
        alertaService.evaluarYRegistrar(registro.withId(idRegistro));
        view.ok("Telemetria registrada. ID: " + idRegistro);
    }
}

