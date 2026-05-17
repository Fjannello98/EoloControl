package ar.edu.siglo21.eolocontrol.view;

import ar.edu.siglo21.eolocontrol.dao.AlertaDao;
import ar.edu.siglo21.eolocontrol.dao.CentralDao;
import ar.edu.siglo21.eolocontrol.dao.ReporteDao;
import ar.edu.siglo21.eolocontrol.dao.TelemetriaDao;
import ar.edu.siglo21.eolocontrol.dao.TurbinaDao;
import ar.edu.siglo21.eolocontrol.model.CentralEolica;
import ar.edu.siglo21.eolocontrol.model.RegistroTelemetria;
import ar.edu.siglo21.eolocontrol.model.TurbinaEolica;
import ar.edu.siglo21.eolocontrol.model.Usuario;
import ar.edu.siglo21.eolocontrol.service.AlertaService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainFrame extends JFrame {
    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final CentralDao centralDao;
    private final TurbinaDao turbinaDao;
    private final TelemetriaDao telemetriaDao;
    private final AlertaDao alertaDao;
    private final ReporteDao reporteDao;
    private final AlertaService alertaService;

    private final DefaultTableModel centralesModel = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Ubicacion", "Provincia"}, 0);
    private final DefaultTableModel turbinasModel = new DefaultTableModel(
            new Object[]{"ID", "Central", "Codigo", "Modelo", "Potencia kW", "Estado"}, 0);
    private final JTextArea reportesArea = new JTextArea();
    private final JTextArea alertasArea = new JTextArea();

    public MainFrame(
            Usuario usuario,
            CentralDao centralDao,
            TurbinaDao turbinaDao,
            TelemetriaDao telemetriaDao,
            AlertaDao alertaDao,
            ReporteDao reporteDao,
            AlertaService alertaService) {
        super("EoloControl Java - " + usuario.nombreUsuario() + " (" + usuario.rol() + ")");
        this.centralDao = centralDao;
        this.turbinaDao = turbinaDao;
        this.telemetriaDao = telemetriaDao;
        this.alertaDao = alertaDao;
        this.reporteDao = reporteDao;
        this.alertaService = alertaService;
        configurar();
        cargarDatos();
    }

    private void configurar() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Centrales", crearPanelCentrales());
        tabs.addTab("Turbinas", crearPanelTurbinas());
        tabs.addTab("Telemetria", crearPanelTelemetria());
        tabs.addTab("Reportes", crearPanelTexto(reportesArea, this::cargarReportes));
        tabs.addTab("Alertas", crearPanelTexto(alertasArea, this::cargarAlertas));

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel crearPanelCentrales() {
        JTextField nombre = new JTextField();
        JTextField ubicacion = new JTextField();
        JTextField provincia = new JTextField();

        JPanel form = formulario(new String[]{"Nombre", "Ubicacion", "Provincia"}, new JTextField[]{nombre, ubicacion, provincia});
        JButton guardar = new JButton("Registrar central");
        JButton actualizar = new JButton("Actualizar listado");

        guardar.addActionListener(event -> ejecutar("Central registrada.", () -> {
            centralDao.crear(new CentralEolica(0, nombre.getText().trim(), ubicacion.getText().trim(), provincia.getText().trim()));
            limpiar(nombre, ubicacion, provincia);
            cargarCentrales();
        }));
        actualizar.addActionListener(event -> cargarCentrales());

        JPanel acciones = new JPanel();
        acciones.add(guardar);
        acciones.add(actualizar);

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(form, BorderLayout.CENTER);
        superior.add(acciones, BorderLayout.SOUTH);

        return panelConTabla(superior, new JTable(centralesModel));
    }

    private JPanel crearPanelTurbinas() {
        JTextField centralId = new JTextField();
        JTextField codigo = new JTextField();
        JTextField modelo = new JTextField();
        JTextField potencia = new JTextField();
        JTextField estado = new JTextField("OPERATIVA");

        JPanel form = formulario(
                new String[]{"ID central", "Codigo", "Modelo", "Potencia maxima kW", "Estado"},
                new JTextField[]{centralId, codigo, modelo, potencia, estado});
        JButton guardar = new JButton("Registrar turbina");
        JButton actualizar = new JButton("Actualizar listado");

        guardar.addActionListener(event -> ejecutar("Turbina registrada.", () -> {
            turbinaDao.crear(new TurbinaEolica(
                    0,
                    Integer.parseInt(centralId.getText().trim()),
                    codigo.getText().trim(),
                    modelo.getText().trim(),
                    decimal(potencia),
                    estado.getText().trim()));
            limpiar(centralId, codigo, modelo, potencia);
            estado.setText("OPERATIVA");
            cargarTurbinas();
        }));
        actualizar.addActionListener(event -> cargarTurbinas());

        JPanel acciones = new JPanel();
        acciones.add(guardar);
        acciones.add(actualizar);

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(form, BorderLayout.CENTER);
        superior.add(acciones, BorderLayout.SOUTH);

        return panelConTabla(superior, new JTable(turbinasModel));
    }

    private JPanel crearPanelTelemetria() {
        JTextField turbinaId = new JTextField();
        JTextField fechaHora = new JTextField(LocalDateTime.now().format(INPUT_DATE));
        JTextField velocidad = new JTextField();
        JTextField direccion = new JTextField("SO");
        JTextField energia = new JTextField();

        JPanel form = formulario(
                new String[]{"ID turbina", "Fecha y hora", "Velocidad viento km/h", "Direccion viento", "Energia generada MWh"},
                new JTextField[]{turbinaId, fechaHora, velocidad, direccion, energia});

        JButton guardar = new JButton("Registrar telemetria");
        guardar.addActionListener(event -> ejecutar("Telemetria registrada.", () -> {
            RegistroTelemetria registro = new RegistroTelemetria(
                    0,
                    Integer.parseInt(turbinaId.getText().trim()),
                    LocalDateTime.parse(fechaHora.getText().trim(), INPUT_DATE),
                    decimal(velocidad),
                    direccion.getText().trim(),
                    decimal(energia));
            int id = telemetriaDao.crear(registro);
            alertaService.evaluarYRegistrar(registro.withId(id));
            limpiar(turbinaId, velocidad, energia);
            fechaHora.setText(LocalDateTime.now().format(INPUT_DATE));
            cargarReportes();
            cargarAlertas();
        }));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.add(new JLabel("Formato de fecha: yyyy-MM-dd HH:mm"), BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(guardar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelTexto(JTextArea area, Runnable actualizar) {
        area.setEditable(false);
        JButton actualizarButton = new JButton("Actualizar");
        actualizarButton.addActionListener(event -> actualizar.run());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(actualizarButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel formulario(String[] etiquetas, JTextField[] campos) {
        JPanel panel = new JPanel(new GridLayout(etiquetas.length, 2, 8, 8));
        for (int i = 0; i < etiquetas.length; i++) {
            panel.add(new JLabel(etiquetas[i]));
            panel.add(campos[i]);
        }
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        return panel;
    }

    private JPanel panelConTabla(JPanel superior, JTable tabla) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.add(superior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private void cargarDatos() {
        cargarCentrales();
        cargarTurbinas();
        cargarReportes();
        cargarAlertas();
    }

    private void cargarCentrales() {
        ejecutarSinMensaje(() -> {
            centralesModel.setRowCount(0);
            for (CentralEolica central : centralDao.listar()) {
                centralesModel.addRow(new Object[]{central.id(), central.nombre(), central.ubicacion(), central.provincia()});
            }
        });
    }

    private void cargarTurbinas() {
        ejecutarSinMensaje(() -> {
            turbinasModel.setRowCount(0);
            for (TurbinaEolica turbina : turbinaDao.listar()) {
                turbinasModel.addRow(new Object[]{
                        turbina.id(),
                        turbina.centralId(),
                        turbina.codigo(),
                        turbina.modelo(),
                        turbina.potenciaMaximaKw(),
                        turbina.estado()});
            }
        });
    }

    private void cargarReportes() {
        ejecutarSinMensaje(() -> escribirLineas(reportesArea, reporteDao.reporteEnergiaPorTurbina()));
    }

    private void cargarAlertas() {
        ejecutarSinMensaje(() -> escribirLineas(alertasArea, alertaDao.listarPendientes()));
    }

    private void escribirLineas(JTextArea area, List<String> lineas) {
        area.setText(String.join(System.lineSeparator(), lineas));
        area.setCaretPosition(0);
    }

    private void ejecutar(String mensajeOk, Runnable accion) {
        try {
            accion.run();
            JOptionPane.showMessageDialog(this, mensajeOk, "Operacion correcta", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarSinMensaje(Runnable accion) {
        try {
            accion.run();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BigDecimal decimal(JTextField campo) {
        return new BigDecimal(campo.getText().trim().replace(",", "."));
    }

    private void limpiar(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }
}
