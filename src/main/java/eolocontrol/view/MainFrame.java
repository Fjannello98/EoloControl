package eolocontrol.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import eolocontrol.controller.ApplicationController;
import eolocontrol.model.CentralEolica;
import eolocontrol.model.RegistroTelemetria;
import eolocontrol.model.TurbinaEolica;
import eolocontrol.model.Usuario;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainFrame extends JFrame {
    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Color COLOR_PRIMARIO = new Color(34, 91, 112);
    private static final Font FONT_BASE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FONT_SUBTITULO = new Font("Segoe UI", Font.BOLD, 16);

    private final ApplicationController controller;

    private final DefaultTableModel centralesModel = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Ubicacion", "Provincia"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column > 0;
        }
    };
    private final DefaultTableModel turbinasModel = new DefaultTableModel(
            new Object[]{"ID", "Central", "Codigo", "Modelo", "Potencia kW", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column > 0;
        }
    };
    private final JTextArea reportesArea = new JTextArea();
    private final JTextArea alertasArea = new JTextArea();
    private final JLabel estadoLabel = new JLabel("Listo.");
    private final JButton temaButton = botonSecundario("Modo oscuro");
    private boolean cargandoTablas;
    private boolean modoOscuro;

    public MainFrame(
            Usuario usuario,
            ApplicationController controller) {
        super("EoloControl Java - " + usuario.nombreUsuario() + " (" + usuario.rol() + ")");
        this.controller = controller;
        configurar();
        cargarDatos();
    }

    private void configurar() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1120, 720);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.addTab("Centrales", crearPanelCentrales());
        tabs.addTab("Turbinas", crearPanelTurbinas());
        tabs.addTab("Telemetria", crearPanelTelemetria());
        tabs.addTab("Reportes", crearPanelTexto(reportesArea, this::cargarReportes));
        tabs.addTab("Alertas", crearPanelTexto(alertasArea, this::cargarAlertas));

        add(crearEncabezado(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(crearBarraEstado(), BorderLayout.SOUTH);
        configurarEdicionTablas();
    }

    private JPanel crearEncabezado() {
        JLabel titulo = new JLabel("EoloControl");
        titulo.setFont(FONT_TITULO);

        JLabel bajada = new JLabel("Panel de monitoreo y gestion operativa de turbinas eolicas");
        bajada.setFont(FONT_BASE);

        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 2));
        panel.add(titulo);
        panel.add(bajada);

        temaButton.addActionListener(event -> alternarTema());

        JPanel contenedor = new JPanel(new BorderLayout(12, 0));
        contenedor.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        contenedor.add(panel, BorderLayout.CENTER);
        contenedor.add(temaButton, BorderLayout.EAST);
        return contenedor;
    }

    private JPanel crearBarraEstado() {
        estadoLabel.setFont(FONT_BASE);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        panel.add(estadoLabel, BorderLayout.WEST);
        return panel;
    }

    private JPanel crearPanelCentrales() {
        JTextField nombre = new JTextField();
        JTextField ubicacion = new JTextField();
        JTextField provincia = new JTextField();

        JPanel form = formulario(new String[]{"Nombre", "Ubicacion", "Provincia"}, new JTextField[]{nombre, ubicacion, provincia});
        JButton guardar = botonPrincipal("Registrar central");
        JButton actualizar = botonSecundario("Actualizar");

        guardar.addActionListener(event -> ejecutar("Central registrada.", () -> {
            controller.crearCentral(new CentralEolica(0, nombre.getText().trim(), ubicacion.getText().trim(), provincia.getText().trim()));
            limpiar(nombre, ubicacion, provincia);
            cargarCentrales();
        }));
        actualizar.addActionListener(event -> cargarCentrales());

        JPanel acciones = acciones(guardar, actualizar);

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBorder(BorderFactory.createEmptyBorder());
        superior.add(tituloPanel("Centrales eolicas", "Carga nuevos datos o edita directamente las filas de la tabla."), BorderLayout.NORTH);
        superior.add(form, BorderLayout.CENTER);
        superior.add(acciones, BorderLayout.SOUTH);

        JTable tabla = tabla(centralesModel);
        return panelConTabla(superior, tabla, "Doble clic sobre Nombre, Ubicacion o Provincia para editar y guardar automaticamente.");
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
        JButton actualizar = botonSecundario("Actualizar");
        JButton buscar = botonSecundario("Buscar por codigo");

        guardar.addActionListener(event -> ejecutar("Turbina registrada.", () -> {
            controller.crearTurbina(new TurbinaEolica(
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
        buscar.addActionListener(event -> buscarTurbinaPorCodigo());

        JPanel acciones = acciones(guardar, buscar, actualizar);

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBorder(BorderFactory.createEmptyBorder());
        superior.add(tituloPanel("Turbinas", "Administra el inventario, busca por codigo o edita las filas de la tabla."), BorderLayout.NORTH);
        superior.add(form, BorderLayout.CENTER);
        superior.add(acciones, BorderLayout.SOUTH);

        JTable tabla = tabla(turbinasModel);
        tabla.getColumnModel().getColumn(5).setCellEditor(new javax.swing.DefaultCellEditor(
                new JComboBox<>(new String[]{"OPERATIVA", "MANTENIMIENTO", "FALLA", "DETENIDA"})));
        return panelConTabla(superior, tabla, "Doble clic para editar Central, Codigo, Modelo, Potencia o Estado. El ID queda protegido.");
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
            controller.registrarTelemetria(registro);
            limpiar(turbinaId, velocidad, energia);
            fechaHora.setText(LocalDateTime.now().format(INPUT_DATE));
            cargarReportes();
            cargarAlertas();
        }));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JPanel bloque = seccion("Nueva medicion", form);
        bloque.add(tituloPanel("Telemetria", "Formato de fecha: yyyy-MM-dd HH:mm"), BorderLayout.NORTH);
        panel.add(bloque, BorderLayout.CENTER);
        panel.add(acciones(guardar), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelTexto(JTextArea area, Runnable actualizar) {
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        area.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JButton actualizarButton = botonSecundario("Actualizar");
        actualizarButton.addActionListener(event -> actualizar.run());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(acciones(actualizarButton), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel formulario(String[] etiquetas, JTextField[] campos) {
        JPanel panel = new JPanel(new GridLayout(etiquetas.length, 2, 10, 10));
        for (int i = 0; i < etiquetas.length; i++) {
            JLabel label = new JLabel(etiquetas[i]);
            label.setFont(FONT_BASE);
            campos[i].setFont(FONT_BASE);
            campos[i].setPreferredSize(new Dimension(190, 30));
            panel.add(label);
            panel.add(campos[i]);
        }
        panel.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        return panel;
    }

    private JPanel panelConTabla(JPanel superior, JTable tabla, String ayuda) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        panel.add(seccion(null, superior), BorderLayout.NORTH);

        JLabel ayudaLabel = new JLabel(ayuda);
        ayudaLabel.setFont(FONT_BASE);

        JPanel tablaPanel = new JPanel(new BorderLayout(0, 8));
        tablaPanel.add(ayudaLabel, BorderLayout.NORTH);
        tablaPanel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(tablaPanel, BorderLayout.CENTER);
        return panel;
    }

    private JTable tabla(DefaultTableModel model) {
        JTable tabla = new JTable(model);
        tabla.setFont(FONT_BASE);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        tabla.setShowVerticalLines(false);
        return tabla;
    }

    private JPanel seccion(String titulo, JPanel contenido) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        if (titulo == null) {
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")),
                    BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        } else {
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(titulo),
                    BorderFactory.createEmptyBorder(8, 10, 10, 10)));
        }
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JPanel tituloPanel(String titulo, String ayuda) {
        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(FONT_SUBTITULO);
        JLabel ayudaLabel = new JLabel(ayuda);
        ayudaLabel.setFont(FONT_BASE);

        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 2));
        panel.add(tituloLabel);
        panel.add(ayudaLabel);
        return panel;
    }

    private JPanel acciones(JButton... botones) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        for (JButton boton : botones) {
            panel.add(boton);
        }
        return panel;
    }

    private JButton botonPrincipal(String texto) {
        JButton boton = new JButton(texto);
        boton.putClientProperty("JButton.buttonType", "roundRect");
        boton.putClientProperty("FlatLaf.style", "background: #225B70; foreground: #FFFFFF; focusColor: #4F93A8; hoverBackground: #2E6E86; pressedBackground: #19495B;");
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return boton;
    }

    private JButton botonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.putClientProperty("JButton.buttonType", "roundRect");
        boton.putClientProperty("FlatLaf.style", "focusColor: #4F93A8;");
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        boton.setFont(FONT_BASE);
        return boton;
    }

    private void alternarTema() {
        try {
            modoOscuro = !modoOscuro;
            if (modoOscuro) {
                FlatDarkLaf.setup();
                temaButton.setText("Modo claro");
                estado("Modo oscuro activado.");
            } else {
                FlatLightLaf.setup();
                temaButton.setText("Modo oscuro");
                estado("Modo claro activado.");
            }
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo cambiar el tema.", "Tema", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarEdicionTablas() {
        centralesModel.addTableModelListener(event -> {
            if (!cargandoTablas && event.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                guardarCentralEditada(event.getFirstRow());
            }
        });
        turbinasModel.addTableModelListener(event -> {
            if (!cargandoTablas && event.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                guardarTurbinaEditada(event.getFirstRow());
            }
        });
    }

    private void cargarDatos() {
        cargarCentrales();
        cargarTurbinas();
        cargarReportes();
        cargarAlertas();
    }

    private void cargarCentrales() {
        ejecutarSinMensaje(() -> {
            cargandoTablas = true;
            centralesModel.setRowCount(0);
            for (CentralEolica central : controller.listarCentrales()) {
                centralesModel.addRow(new Object[]{central.id(), central.nombre(), central.ubicacion(), central.provincia()});
            }
            cargandoTablas = false;
            estado("Centrales actualizadas.");
        });
    }

    private void cargarTurbinas() {
        ejecutarSinMensaje(() -> {
            cargandoTablas = true;
            turbinasModel.setRowCount(0);
            // La vista recibe las turbinas desde el controlador, ya ordenadas por la logica de negocio.
            List<TurbinaEolica> turbinas = controller.listarTurbinas();
            for (TurbinaEolica turbina : turbinas) {
                turbinasModel.addRow(new Object[]{
                        turbina.id(),
                        turbina.centralId(),
                        turbina.codigo(),
                        turbina.modelo(),
                        turbina.potenciaMaximaKw(),
                        turbina.estado()});
            }
            cargandoTablas = false;
            estado("Turbinas actualizadas.");
        });
    }

    private void guardarCentralEditada(int row) {
        if (row < 0 || row >= centralesModel.getRowCount()) {
            return;
        }
        try {
            CentralEolica central = new CentralEolica(
                    entero(centralesModel.getValueAt(row, 0)),
                    texto(centralesModel.getValueAt(row, 1)),
                    texto(centralesModel.getValueAt(row, 2)),
                    texto(centralesModel.getValueAt(row, 3)));
            controller.actualizarCentral(central);
            estado("Central actualizada automaticamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo editar la central", JOptionPane.ERROR_MESSAGE);
            cargarCentrales();
        }
    }

    private void guardarTurbinaEditada(int row) {
        if (row < 0 || row >= turbinasModel.getRowCount()) {
            return;
        }
        try {
            TurbinaEolica turbina = new TurbinaEolica(
                    entero(turbinasModel.getValueAt(row, 0)),
                    entero(turbinasModel.getValueAt(row, 1)),
                    texto(turbinasModel.getValueAt(row, 2)),
                    texto(turbinasModel.getValueAt(row, 3)),
                    decimal(turbinasModel.getValueAt(row, 4)),
                    texto(turbinasModel.getValueAt(row, 5)));
            controller.actualizarTurbina(turbina);
            estado("Turbina actualizada automaticamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo editar la turbina", JOptionPane.ERROR_MESSAGE);
            cargarTurbinas();
        }
    }

    private void buscarTurbinaPorCodigo() {
        ejecutarSinMensaje(() -> {
            String codigo = JOptionPane.showInputDialog(this, "Ingrese el codigo de la turbina");
            if (codigo == null) {
                return;
            }
            // Optional permite expresar claramente si la busqueda encontro o no una turbina.
            controller.buscarTurbinaPorCodigo(codigo)
                    .ifPresentOrElse(
                            turbina -> JOptionPane.showMessageDialog(this, turbina.resumenOperativo(), "Turbina encontrada", JOptionPane.INFORMATION_MESSAGE),
                            () -> JOptionPane.showMessageDialog(this, "No se encontro una turbina con ese codigo.", "Busqueda sin resultados", JOptionPane.WARNING_MESSAGE));
        });
    }

    private void cargarReportes() {
        ejecutarSinMensaje(() -> escribirLineas(reportesArea, controller.generarReporteEnergia()));
    }

    private void cargarAlertas() {
        ejecutarSinMensaje(() -> escribirLineas(alertasArea, controller.listarAlertasPendientes()));
    }

    private void escribirLineas(JTextArea area, List<String> lineas) {
        area.setText(String.join(System.lineSeparator(), lineas));
        area.setCaretPosition(0);
    }

    private void ejecutar(String mensajeOk, Runnable accion) {
        try {
            accion.run();
            estado(mensajeOk);
            JOptionPane.showMessageDialog(this, mensajeOk, "Operacion correcta", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarSinMensaje(Runnable accion) {
        try {
            accion.run();
        } catch (Exception ex) {
            cargandoTablas = false;
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BigDecimal decimal(JTextField campo) {
        return new BigDecimal(campo.getText().trim().replace(",", "."));
    }

    private BigDecimal decimal(Object valor) {
        return new BigDecimal(texto(valor).replace(",", "."));
    }

    private int entero(Object valor) {
        return Integer.parseInt(texto(valor));
    }

    private String texto(Object valor) {
        return valor == null ? "" : valor.toString().trim();
    }

    private void estado(String mensaje) {
        estadoLabel.setText(mensaje);
    }

    private void limpiar(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }
}

