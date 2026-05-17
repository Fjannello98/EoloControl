package eolocontrol.view;

import eolocontrol.dao.AlertaDao;
import eolocontrol.dao.CentralDao;
import eolocontrol.dao.ReporteDao;
import eolocontrol.dao.TelemetriaDao;
import eolocontrol.dao.TurbinaDao;
import eolocontrol.model.CentralEolica;
import eolocontrol.model.RegistroTelemetria;
import eolocontrol.model.TurbinaEolica;
import eolocontrol.model.Usuario;
import eolocontrol.service.AlertaService;

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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainFrame extends JFrame {
    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Color COLOR_FONDO = new Color(244, 247, 246);
    private static final Color COLOR_PANEL = Color.WHITE;
    private static final Color COLOR_PRIMARIO = new Color(28, 115, 107);
    private static final Color COLOR_TEXTO = new Color(36, 48, 56);
    private static final Font FONT_BASE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 22);

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
        setSize(980, 620);
        getContentPane().setBackground(COLOR_FONDO);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(COLOR_FONDO);
        tabs.addTab("Centrales", crearPanelCentrales());
        tabs.addTab("Turbinas", crearPanelTurbinas());
        tabs.addTab("Telemetria", crearPanelTelemetria());
        tabs.addTab("Reportes", crearPanelTexto(reportesArea, this::cargarReportes));
        tabs.addTab("Alertas", crearPanelTexto(alertasArea, this::cargarAlertas));

        add(crearEncabezado(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
        JLabel titulo = new JLabel("EoloControl");
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(Color.WHITE);

        JLabel bajada = new JLabel("Panel de monitoreo y gestion operativa de turbinas eolicas");
        bajada.setFont(FONT_BASE);
        bajada.setForeground(new Color(216, 235, 232));

        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 2));
        panel.setBackground(COLOR_PRIMARIO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        panel.add(titulo);
        panel.add(bajada);
        return panel;
    }

    private JPanel crearPanelCentrales() {
        JTextField nombre = new JTextField();
        JTextField ubicacion = new JTextField();
        JTextField provincia = new JTextField();

        JPanel form = formulario(new String[]{"Nombre", "Ubicacion", "Provincia"}, new JTextField[]{nombre, ubicacion, provincia});
        JButton guardar = botonPrincipal("Registrar central");
        JButton actualizar = botonSecundario("Actualizar listado");

        guardar.addActionListener(event -> ejecutar("Central registrada.", () -> {
            centralDao.crear(new CentralEolica(0, nombre.getText().trim(), ubicacion.getText().trim(), provincia.getText().trim()));
            limpiar(nombre, ubicacion, provincia);
            cargarCentrales();
        }));
        actualizar.addActionListener(event -> cargarCentrales());

        JPanel acciones = new JPanel();
        acciones.setBackground(COLOR_PANEL);
        acciones.add(guardar);
        acciones.add(actualizar);

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(COLOR_PANEL);
        superior.setBorder(BorderFactory.createTitledBorder("Datos de la central"));
        superior.add(form, BorderLayout.CENTER);
        superior.add(acciones, BorderLayout.SOUTH);

        return panelConTabla(superior, tabla(centralesModel));
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
        JButton guardar = botonPrincipal("Registrar turbina");
        JButton actualizar = botonSecundario("Actualizar listado");

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
        acciones.setBackground(COLOR_PANEL);
        acciones.add(guardar);
        acciones.add(actualizar);

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(COLOR_PANEL);
        superior.setBorder(BorderFactory.createTitledBorder("Datos de la turbina"));
        superior.add(form, BorderLayout.CENTER);
        superior.add(acciones, BorderLayout.SOUTH);

        return panelConTabla(superior, tabla(turbinasModel));
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

        JButton guardar = botonPrincipal("Registrar telemetria");
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
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JPanel bloque = seccion("Nueva medicion", form);
        bloque.add(new JLabel("Formato de fecha: yyyy-MM-dd HH:mm"), BorderLayout.NORTH);
        panel.add(bloque, BorderLayout.CENTER);
        panel.add(guardar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelTexto(JTextArea area, Runnable actualizar) {
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        area.setForeground(COLOR_TEXTO);
        area.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JButton actualizarButton = botonSecundario("Actualizar");
        actualizarButton.addActionListener(event -> actualizar.run());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(actualizarButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel formulario(String[] etiquetas, JTextField[] campos) {
        JPanel panel = new JPanel(new GridLayout(etiquetas.length, 2, 8, 8));
        panel.setBackground(COLOR_PANEL);
        for (int i = 0; i < etiquetas.length; i++) {
            JLabel label = new JLabel(etiquetas[i]);
            label.setFont(FONT_BASE);
            label.setForeground(COLOR_TEXTO);
            campos[i].setFont(FONT_BASE);
            campos[i].setPreferredSize(new Dimension(180, 28));
            panel.add(label);
            panel.add(campos[i]);
        }
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        return panel;
    }

    private JPanel panelConTabla(JPanel superior, JTable tabla) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        panel.add(superior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private JTable tabla(DefaultTableModel model) {
        JTable tabla = new JTable(model);
        tabla.setFont(FONT_BASE);
        tabla.setRowHeight(28);
        tabla.setGridColor(new Color(225, 232, 230));
        tabla.setSelectionBackground(new Color(214, 235, 231));
        tabla.setSelectionForeground(COLOR_TEXTO);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(new Color(229, 239, 237));
        tabla.getTableHeader().setForeground(COLOR_TEXTO);
        return tabla;
    }

    private JPanel seccion(String titulo, JPanel contenido) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JButton botonPrincipal(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(COLOR_PRIMARIO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return boton;
    }

    private JButton botonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setFont(FONT_BASE);
        return boton;
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

