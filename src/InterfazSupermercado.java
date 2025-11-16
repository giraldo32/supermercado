import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;

/**
 * InterfazSupermercado.java
 * Interfaz gráfica profesional para la simulación de cobro en supermercado
 */
public class InterfazSupermercado extends JFrame {
    
    // Componentes principales
    private JTextArea txtResultados;
    private JTable tablaCatalogo;
    private DefaultTableModel modeloCatalogo;
    private JTable tablaClientes;
    private DefaultTableModel modeloClientes;
    private JSpinner spinnerCajeras;
    private JButton btnIniciarSimulacion;
    private JButton btnLimpiar;
    private JProgressBar progressBar;
    private JLabel lblEstado;
    private JLabel lblTiempoTotal;
    private JLabel lblMontoTotal;
    private JLabel lblClientesAtendidos;
    
    // Datos
    private List<Producto> catalogoProductos;
    private List<ClienteInfo> clientesInfo;
    private boolean simulacionEnCurso = false;
    
    // Clase auxiliar para almacenar info de clientes
    private static class ClienteInfo {
        String nombre;
        List<Producto> productos;
        
        ClienteInfo(String nombre, List<Producto> productos) {
            this.nombre = nombre;
            this.productos = productos;
        }
        
        double getTotal() {
            return productos.stream().mapToDouble(Producto::getPrecio).sum();
        }
    }
    
    public InterfazSupermercado() {
        catalogoProductos = new ArrayList<>();
        clientesInfo = new ArrayList<>();
        
        configurarVentana();
        inicializarComponentes();
        cargarProductosIniciales();
    }
    
    private void configurarVentana() {
        setTitle("Sistema de Simulación de Cobro - Supermercado");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Icono y tema moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void inicializarComponentes() {
        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(245, 245, 245));
        
        // Panel superior: Título y estadísticas
        panelPrincipal.add(crearPanelSuperior(), BorderLayout.NORTH);
        
        // Panel central: Dividido en izquierda (configuración) y derecha (resultados)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(crearPanelConfiguracion());
        splitPane.setRightComponent(crearPanelResultados());
        splitPane.setDividerLocation(650);
        splitPane.setResizeWeight(0.5);
        panelPrincipal.add(splitPane, BorderLayout.CENTER);
        
        // Panel inferior: Controles
        panelPrincipal.add(crearPanelControles(), BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(41, 128, 185));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Título
        JLabel lblTitulo = new JLabel("🛒 Sistema de Simulación de Cobro - Supermercado");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panel.add(lblTitulo, BorderLayout.WEST);
        
        // Panel de estadísticas
        JPanel panelEstadisticas = new JPanel(new GridLayout(1, 3, 10, 0));
        panelEstadisticas.setOpaque(false);
        
        lblClientesAtendidos = crearLabelEstadistica("Clientes: 0");
        lblTiempoTotal = crearLabelEstadistica("Tiempo: 0.0 s");
        lblMontoTotal = crearLabelEstadistica("Total: $0.00");
        
        panelEstadisticas.add(lblClientesAtendidos);
        panelEstadisticas.add(lblTiempoTotal);
        panelEstadisticas.add(lblMontoTotal);
        
        panel.add(panelEstadisticas, BorderLayout.EAST);
        
        return panel;
    }
    
    private JLabel crearLabelEstadistica(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.BLACK);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }
    
    private JPanel crearPanelConfiguracion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Panel con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        tabbedPane.addTab("📦 Catálogo de Productos", crearPanelCatalogo());
        tabbedPane.addTab("👥 Clientes y Compras", crearPanelClientes());
        tabbedPane.addTab("⚙️ Configuración", crearPanelConfiguracionCajeras());
        
        panel.add(tabbedPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Tabla de productos
        String[] columnas = {"Producto", "Precio ($)", "Tiempo Procesamiento (ms)"};
        modeloCatalogo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCatalogo = new JTable(modeloCatalogo);
        tablaCatalogo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaCatalogo.setRowHeight(25);
        tablaCatalogo.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaCatalogo.getTableHeader().setBackground(new Color(52, 152, 219));
        tablaCatalogo.getTableHeader().setForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(tablaCatalogo);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnAgregar = crearBoton("➕ Agregar Producto", new Color(46, 204, 113));
        btnAgregar.addActionListener(e -> mostrarDialogoAgregarProducto());
        
        JButton btnEliminar = crearBoton("🗑️ Eliminar Producto", new Color(231, 76, 60));
        btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Tabla de clientes
        String[] columnas = {"Cliente", "# Productos", "Total ($)"};
        modeloClientes = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaClientes = new JTable(modeloClientes);
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaClientes.setRowHeight(25);
        tablaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaClientes.getTableHeader().setBackground(new Color(155, 89, 182));
        tablaClientes.getTableHeader().setForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnAgregar = crearBoton("➕ Agregar Cliente", new Color(46, 204, 113));
        btnAgregar.addActionListener(e -> mostrarDialogoAgregarCliente());
        
        JButton btnEliminar = crearBoton("🗑️ Eliminar Cliente", new Color(231, 76, 60));
        btnEliminar.addActionListener(e -> eliminarClienteSeleccionado());
        
        JButton btnGenerarAleatorio = crearBoton("🎲 Generar Aleatorio", new Color(52, 152, 219));
        btnGenerarAleatorio.addActionListener(e -> generarClienteAleatorio());
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnGenerarAleatorio);
        
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelConfiguracionCajeras() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Número de cajeras
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblCajeras = new JLabel("Número de Cajeras:");
        lblCajeras.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lblCajeras, gbc);
        
        gbc.gridx = 1;
        spinnerCajeras = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        spinnerCajeras.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) spinnerCajeras.getEditor()).getTextField().setColumns(5);
        panel.add(spinnerCajeras, gbc);
        
        // Información
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JTextArea txtInfo = new JTextArea(
            "Configuración de la Simulación:\n\n" +
            "• Agregue productos al catálogo con sus precios\n" +
            "  y tiempo de procesamiento.\n\n" +
            "• Agregue clientes con sus productos a comprar.\n\n" +
            "• Configure el número de cajeras (hilos) que\n" +
            "  atenderán simultáneamente.\n\n" +
            "• Presione 'Iniciar Simulación' para comenzar."
        );
        txtInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtInfo.setEditable(false);
        txtInfo.setBackground(new Color(250, 250, 250));
        txtInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.add(txtInfo, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelResultados() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Título
        JLabel lblTitulo = new JLabel("📊 Resultados de la Simulación");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        // Área de texto para resultados
        txtResultados = new JTextArea();
        txtResultados.setFont(new Font("Consolas", Font.PLAIN, 11));
        txtResultados.setEditable(false);
        txtResultados.setBackground(new Color(30, 30, 30));
        txtResultados.setForeground(new Color(0, 255, 0));
        txtResultados.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(txtResultados);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelControles() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Estado y progress bar
        JPanel panelEstado = new JPanel(new BorderLayout(5, 5));
        panelEstado.setOpaque(false);
        
        lblEstado = new JLabel("Listo para iniciar");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelEstado.add(lblEstado, BorderLayout.NORTH);
        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panelEstado.add(progressBar, BorderLayout.CENTER);
        
        panel.add(panelEstado, BorderLayout.CENTER);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setOpaque(false);
        
        btnLimpiar = crearBoton("🗑️ Limpiar Resultados", new Color(149, 165, 166));
        btnLimpiar.addActionListener(e -> limpiarResultados());
        
        btnIniciarSimulacion = crearBoton("▶️ Iniciar Simulación", new Color(46, 204, 113));
        btnIniciarSimulacion.addActionListener(e -> iniciarSimulacion());
        
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnIniciarSimulacion);
        
        panel.add(panelBotones, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 35));
        
        // Efecto hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }
    
    private void cargarProductosIniciales() {
        // Productos por defecto
        catalogoProductos.add(new Producto("Arroz 1kg", 4.20, 500));
        catalogoProductos.add(new Producto("Leche 1L", 1.80, 300));
        catalogoProductos.add(new Producto("Pan", 0.90, 200));
        catalogoProductos.add(new Producto("Huevos (12)", 3.50, 400));
        catalogoProductos.add(new Producto("Pollo 1kg", 6.50, 800));
        catalogoProductos.add(new Producto("Café 250g", 5.00, 600));
        catalogoProductos.add(new Producto("Aceite 1L", 7.20, 700));
        
        actualizarTablaCatalogo();
    }
    
    private void actualizarTablaCatalogo() {
        modeloCatalogo.setRowCount(0);
        for (Producto p : catalogoProductos) {
            modeloCatalogo.addRow(new Object[]{
                p.getNombre(),
                String.format("%.2f", p.getPrecio()),
                p.getTiempoProcesamientoMs()
            });
        }
    }
    
    private void actualizarTablaClientes() {
        modeloClientes.setRowCount(0);
        for (ClienteInfo ci : clientesInfo) {
            modeloClientes.addRow(new Object[]{
                ci.nombre,
                ci.productos.size(),
                String.format("%.2f", ci.getTotal())
            });
        }
    }
    
    private void mostrarDialogoAgregarProducto() {
        JDialog dialog = new JDialog(this, "Agregar Producto", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        JTextField txtNombre = new JTextField(20);
        dialog.add(txtNombre, gbc);
        
        // Precio
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Precio ($):"), gbc);
        gbc.gridx = 1;
        JTextField txtPrecio = new JTextField(20);
        dialog.add(txtPrecio, gbc);
        
        // Tiempo
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Tiempo (ms):"), gbc);
        gbc.gridx = 1;
        JTextField txtTiempo = new JTextField(20);
        dialog.add(txtTiempo, gbc);
        
        // Botones
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel();
        
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                long tiempo = Long.parseLong(txtTiempo.getText().trim());
                
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Ingrese un nombre válido", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                catalogoProductos.add(new Producto(nombre, precio, tiempo));
                actualizarTablaCatalogo();
                dialog.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Ingrese valores numéricos válidos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        dialog.add(panelBotones, gbc);
        
        dialog.setVisible(true);
    }
    
    private void eliminarProductoSeleccionado() {
        int selectedRow = tablaCatalogo.getSelectedRow();
        if (selectedRow >= 0) {
            catalogoProductos.remove(selectedRow);
            actualizarTablaCatalogo();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void mostrarDialogoAgregarCliente() {
        if (catalogoProductos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue productos al catálogo primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(this, "Agregar Cliente", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTop.add(new JLabel("Nombre del Cliente:"));
        JTextField txtNombre = new JTextField(20);
        panelTop.add(txtNombre);
        dialog.add(panelTop, BorderLayout.NORTH);
        
        // Lista de productos con checkboxes
        JPanel panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        
        List<JCheckBox> checkboxes = new ArrayList<>();
        for (Producto p : catalogoProductos) {
            JCheckBox cb = new JCheckBox(p.toString());
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            checkboxes.add(cb);
            panelProductos.add(cb);
        }
        
        JScrollPane scrollPane = new JScrollPane(panelProductos);
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Ingrese un nombre para el cliente", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<Producto> productosSeleccionados = new ArrayList<>();
            for (int i = 0; i < checkboxes.size(); i++) {
                if (checkboxes.get(i).isSelected()) {
                    productosSeleccionados.add(catalogoProductos.get(i));
                }
            }
            
            if (productosSeleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Seleccione al menos un producto", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            clientesInfo.add(new ClienteInfo(nombre, productosSeleccionados));
            actualizarTablaClientes();
            dialog.dispose();
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        dialog.add(panelBotones, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void eliminarClienteSeleccionado() {
        int selectedRow = tablaClientes.getSelectedRow();
        if (selectedRow >= 0) {
            clientesInfo.remove(selectedRow);
            actualizarTablaClientes();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void generarClienteAleatorio() {
        if (catalogoProductos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue productos al catálogo primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Random rnd = new Random();
        int numProductos = rnd.nextInt(4) + 1;
        List<Producto> productos = new ArrayList<>();
        
        for (int i = 0; i < numProductos; i++) {
            productos.add(catalogoProductos.get(rnd.nextInt(catalogoProductos.size())));
        }
        
        String nombre = "Cliente-" + (clientesInfo.size() + 1);
        clientesInfo.add(new ClienteInfo(nombre, productos));
        actualizarTablaClientes();
    }
    
    private void limpiarResultados() {
        txtResultados.setText("");
        lblClientesAtendidos.setText("Clientes: 0");
        lblTiempoTotal.setText("Tiempo: 0.0 s");
        lblMontoTotal.setText("Total: $0.00");
        progressBar.setValue(0);
        lblEstado.setText("Listo para iniciar");
    }
    
    private void iniciarSimulacion() {
        if (simulacionEnCurso) {
            JOptionPane.showMessageDialog(this, "Ya hay una simulación en curso", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (clientesInfo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un cliente para la simulación", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        simulacionEnCurso = true;
        btnIniciarSimulacion.setEnabled(false);
        txtResultados.setText("");
        progressBar.setValue(0);
        progressBar.setMaximum(clientesInfo.size());
        lblEstado.setText("Simulación en curso...");
        
        // Ejecutar simulación en un thread separado
        new Thread(() -> {
            try {
                ejecutarSimulacion();
            } finally {
                SwingUtilities.invokeLater(() -> {
                    simulacionEnCurso = false;
                    btnIniciarSimulacion.setEnabled(true);
                    lblEstado.setText("Simulación completada");
                    progressBar.setValue(progressBar.getMaximum());
                });
            }
        }).start();
    }
    
    private void ejecutarSimulacion() {
        int numCajeras = (Integer) spinnerCajeras.getValue();
        
        BlockingQueue<Cliente> colaClientes = new LinkedBlockingQueue<>();
        AtomicLong tiempoTotalGlobalMs = new AtomicLong(0);
        DoubleAdder montoTotalGlobal = new DoubleAdder();
        
        // Crear clientes y encolarlos
        for (ClienteInfo ci : clientesInfo) {
            Cliente cliente = new Cliente(ci.nombre, ci.productos);
            try {
                colaClientes.put(cliente);
                agregarLog(String.format("[Sistema] Encolado %s: %d productos, total=$%.2f",
                    cliente.getNombre(), ci.productos.size(), ci.getTotal()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Crear y arrancar cajeras
        List<Thread> hilosCajeras = new ArrayList<>();
        for (int c = 1; c <= numCajeras; c++) {
            CajeraConCallback cajera = new CajeraConCallback(
                "Cajera-" + c,
                colaClientes,
                tiempoTotalGlobalMs,
                montoTotalGlobal,
                this::agregarLog,
                this::actualizarProgreso
            );
            Thread t = new Thread(cajera);
            t.start();
            hilosCajeras.add(t);
        }
        
        // Señal de fin
        try {
            Thread.sleep(500);
            Cliente fin = new Cliente("FIN", Collections.emptyList());
            colaClientes.put(fin);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Esperar a que terminen todas las cajeras
        for (Thread t : hilosCajeras) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Mostrar resultados finales
        agregarLog("\n" + "=".repeat(80));
        agregarLog(String.format("RESULTADOS FINALES DE LA SIMULACIÓN"));
        agregarLog("=".repeat(80));
        agregarLog(String.format("Clientes atendidos: %d", clientesInfo.size()));
        agregarLog(String.format("Tiempo total de cobro: %d ms (%.2f s)",
            tiempoTotalGlobalMs.get(), tiempoTotalGlobalMs.get() / 1000.0));
        agregarLog(String.format("Monto total facturado: $%.2f", montoTotalGlobal.doubleValue()));
        agregarLog("=".repeat(80));
        
        // Actualizar estadísticas en la UI
        SwingUtilities.invokeLater(() -> {
            lblClientesAtendidos.setText("Clientes: " + clientesInfo.size());
            lblTiempoTotal.setText(String.format("Tiempo: %.2f s", tiempoTotalGlobalMs.get() / 1000.0));
            lblMontoTotal.setText(String.format("Total: $%.2f", montoTotalGlobal.doubleValue()));
        });
    }
    
    private void agregarLog(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            txtResultados.append(mensaje + "\n");
            txtResultados.setCaretPosition(txtResultados.getDocument().getLength());
        });
    }
    
    private void actualizarProgreso() {
        SwingUtilities.invokeLater(() -> {
            int valor = progressBar.getValue();
            if (valor < progressBar.getMaximum()) {
                progressBar.setValue(valor + 1);
            }
        });
    }
    
    // Clase Cajera modificada para enviar logs a la interfaz
    private class CajeraConCallback implements Runnable {
        private final String nombre;
        private final BlockingQueue<Cliente> colaClientes;
        private final AtomicLong tiempoTotalGlobalMs;
        private final DoubleAdder montoTotalGlobal;
        private final java.util.function.Consumer<String> logCallback;
        private final Runnable progresoCallback;
        
        public CajeraConCallback(String nombre,
                                BlockingQueue<Cliente> colaClientes,
                                AtomicLong tiempoTotalGlobalMs,
                                DoubleAdder montoTotalGlobal,
                                java.util.function.Consumer<String> logCallback,
                                Runnable progresoCallback) {
            this.nombre = nombre;
            this.colaClientes = colaClientes;
            this.tiempoTotalGlobalMs = tiempoTotalGlobalMs;
            this.montoTotalGlobal = montoTotalGlobal;
            this.logCallback = logCallback;
            this.progresoCallback = progresoCallback;
        }
        
        @Override
        public void run() {
            try {
                while (true) {
                    Cliente cliente = colaClientes.take();
                    
                    if (cliente.getNombre().equals("FIN")) {
                        colaClientes.put(cliente);
                        logCallback.accept(String.format("[%s] Recibe señal de FIN y termina.", nombre));
                        break;
                    }
                    
                    logCallback.accept(String.format("[%s] ► Inicia cobro de %s", nombre, cliente.getNombre()));
                    long tInicio = System.currentTimeMillis();
                    
                    for (Producto p : cliente.getProductos()) {
                        logCallback.accept(String.format("  [%s] → Procesando: %s — Precio: $%.2f — Tiempo: %d ms",
                            nombre, p.getNombre(), p.getPrecio(), p.getTiempoProcesamientoMs()));
                        try {
                            Thread.sleep(p.getTiempoProcesamientoMs());
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            logCallback.accept(String.format("[%s] Interrumpido", nombre));
                        }
                    }
                    
                    long tFin = System.currentTimeMillis();
                    long tiempoClienteMs = tFin - tInicio;
                    double monto = cliente.totalCompra();
                    
                    tiempoTotalGlobalMs.addAndGet(tiempoClienteMs);
                    montoTotalGlobal.add(monto);
                    
                    logCallback.accept(String.format("[%s] ✓ Finaliza cobro de %s — Tiempo: %d ms (%.2f s) — Total: $%.2f",
                        nombre, cliente.getNombre(), tiempoClienteMs, tiempoClienteMs / 1000.0, monto));
                    
                    progresoCallback.run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logCallback.accept(String.format("[%s] Hilo interrumpido", nombre));
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazSupermercado frame = new InterfazSupermercado();
            frame.setVisible(true);
        });
    }
}
