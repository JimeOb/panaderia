package src.java.ui;

import src.java.excepciones.CostoInvalidoException;
import src.java.excepciones.ValorNegativoException;
import src.java.modelo.Galleta;
import src.java.modelo.Pan;
import src.java.modelo.Producto;
import src.java.persistencia.GalletaDAO;
import src.java.persistencia.PanDAO;
import src.java.servicios.AdministradorProductos;
import src.java.utilidades.reporteCSV;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class interfazGrafica {
    private AdministradorProductos administrador;
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;

    public interfazGrafica(AdministradorProductos administrador) {
        this.administrador = administrador;
        crearInterfaz();
    }

    private void crearInterfaz() {
        frame = new JFrame("Gestión de Productos - Panadería");
        frame.setSize(900, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Configuración del JTable y su modelo.
        String[] columnNames = {"ID", "Nombre", "Cantidad", "Precio Venta", "Costo Producción", "Tipo", "Detalle"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no serán editables
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel de filtros y reporte (botones de filtrado por tipo y generación de CSV)
        JPanel panelFiltros = new JPanel();
        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        JButton btnFiltrarPan = new JButton("Filtrar Pan");
        JButton btnFiltrarGalleta = new JButton("Filtrar Galleta");
        JButton btnGenerarCSV = new JButton("Generar Reporte CSV");

        btnMostrarTodos.addActionListener((ActionEvent e) ->
                actualizarTabla(administrador.getProductos())
        );
        btnFiltrarPan.addActionListener((ActionEvent e) -> {
            List<Producto> filtrados = administrador.getProductos().stream()
                    .filter(p -> p instanceof Pan)
                    .collect(Collectors.toList());
            actualizarTabla(filtrados);
        });
        btnFiltrarGalleta.addActionListener((ActionEvent e) -> {
            List<Producto> filtrados = administrador.getProductos().stream()
                    .filter(p -> p instanceof Galleta)
                    .collect(Collectors.toList());
            actualizarTabla(filtrados);
        });
        btnGenerarCSV.addActionListener((ActionEvent e) -> {
            reporteCSV.generarReporteCSV(administrador.getProductos(), "reporte_productos.csv");
            JOptionPane.showMessageDialog(frame, "Reporte CSV generado.");
        });
        panelFiltros.add(btnMostrarTodos);
        panelFiltros.add(btnFiltrarPan);
        panelFiltros.add(btnFiltrarGalleta);
        panelFiltros.add(btnGenerarCSV);

        // Panel CRUD para agregar, editar y eliminar productos.
        JPanel panelCrud = new JPanel();
        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnEditar = new JButton("Editar Producto");
        JButton btnEliminar = new JButton("Eliminar Producto");

        btnAgregar.addActionListener((ActionEvent e) -> agregarProducto());
        btnEditar.addActionListener((ActionEvent e) -> editarProducto());
        btnEliminar.addActionListener((ActionEvent e) -> eliminarProducto());

        panelCrud.add(btnAgregar);
        panelCrud.add(btnEditar);
        panelCrud.add(btnEliminar);

        // Panel inferior que agrupa filtros y CRUD en dos filas.
        JPanel panelInferior = new JPanel(new GridLayout(2, 1));
        panelInferior.add(panelFiltros);
        panelInferior.add(panelCrud);
        frame.add(panelInferior, BorderLayout.SOUTH);

        // Al iniciar, refrescar datos de la base de datos.
        refrescarDatos();
        actualizarTabla(administrador.getProductos());
    }

    // Actualiza la tabla con la lista de productos.
    private void actualizarTabla(List<Producto> productos) {
        tableModel.setRowCount(0); // Limpia las filas existentes.
        for (Producto p : productos) {
            String tipo;
            String detalle;
            if (p instanceof Pan) {
                tipo = "Pan";
                detalle = "Tiene Queso: " + ((Pan) p).isTieneQueso();
            } else if (p instanceof Galleta) {
                tipo = "Galleta";
                detalle = "Tiene Chispas: " + ((Galleta) p).isTieneChispasChocolate();
            } else {
                tipo = "Desconocido";
                detalle = "";
            }
            Object[] fila = {
                    p.getIdProducto(),
                    p.getNombre(),
                    p.getCantidad(),
                    p.getPrecioVenta(),
                    p.getCostoProduccion(),
                    tipo,
                    detalle
            };
            tableModel.addRow(fila);
        }
    }

    // Refresca la lista de productos del Administrador consultando la BD mediante los DAOs.
    private void refrescarDatos() {
        try {
            PanDAO panDAO = new PanDAO();
            GalletaDAO galletaDAO = new GalletaDAO();
            List<Pan> pansBD = panDAO.listarPanes();
            List<Galleta> galletasBD = galletaDAO.listarGalletas();
            administrador.getProductos().clear();
            for (Pan p : pansBD) {
                administrador.agregarProducto(p);
            }
            for (Galleta g : galletasBD) {
                administrador.agregarProducto(g);
            }
        } catch (SQLException | CostoInvalidoException | ValorNegativoException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al refrescar datos: " + ex.getMessage());
        }
    }

    // Método para agregar un producto: muestra el diálogo, inserta en la BD y refresca.
    private void agregarProducto() {
        try {
            Producto nuevoProducto = mostrarDialogoProducto(null);
            if (nuevoProducto != null) {
                if (nuevoProducto instanceof Pan) {
                    PanDAO panDAO = new PanDAO();
                    panDAO.insertarPan((Pan) nuevoProducto);
                } else if (nuevoProducto instanceof Galleta) {
                    GalletaDAO galletaDAO = new GalletaDAO();
                    galletaDAO.insertarGalleta((Galleta) nuevoProducto);
                }
                refrescarDatos();
                actualizarTabla(administrador.getProductos());
                JOptionPane.showMessageDialog(frame, "Producto agregado con éxito.");
            }
        } catch (SQLException | CostoInvalidoException | ValorNegativoException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al guardar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para editar el producto seleccionado: muestra diálogo, actualiza en BD y refresca.
    private void editarProducto() {
        int filaSeleccionada = table.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int id = (int) tableModel.getValueAt(filaSeleccionada, 0);
            Optional<Producto> prodOpt = administrador.getProductos().stream()
                    .filter(p -> p.getIdProducto() == id)
                    .findFirst();
            if (prodOpt.isPresent()) {
                Producto productoOriginal = prodOpt.get();
                try {
                    Producto productoEditado = mostrarDialogoProducto(productoOriginal);
                    if (productoEditado != null) {
                        if (productoOriginal instanceof Pan) {
                            PanDAO panDAO = new PanDAO();
                            Pan panEditado = (Pan) productoEditado;
                            panEditado.setIdProducto(productoOriginal.getIdProducto());
                            panDAO.actualizarPan(panEditado);
                        } else if (productoOriginal instanceof Galleta) {
                            GalletaDAO galletaDAO = new GalletaDAO();
                            Galleta galletaEditada = (Galleta) productoEditado;
                            galletaEditada.setIdProducto(productoOriginal.getIdProducto());
                            galletaDAO.actualizarGalleta(galletaEditada);
                        }
                        refrescarDatos();
                        actualizarTabla(administrador.getProductos());
                        JOptionPane.showMessageDialog(frame, "Producto actualizado con éxito.");
                    }
                } catch (SQLException | CostoInvalidoException | ValorNegativoException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error al actualizar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Seleccione un producto para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Método para eliminar el producto seleccionado: elimina en BD y refresca.
    private void eliminarProducto() {
        int filaSeleccionada = table.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int confirmacion = JOptionPane.showConfirmDialog(frame, "¿Está seguro de eliminar el producto seleccionado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                int id = (int) tableModel.getValueAt(filaSeleccionada, 0);
                try {
                    Producto productoAEliminar = administrador.getProductos().stream()
                            .filter(p -> p.getIdProducto() == id)
                            .findFirst().orElse(null);
                    if (productoAEliminar != null) {
                        if (productoAEliminar instanceof Pan) {
                            PanDAO panDAO = new PanDAO();
                            panDAO.eliminarPan(id);
                        } else if (productoAEliminar instanceof Galleta) {
                            GalletaDAO galletaDAO = new GalletaDAO();
                            galletaDAO.eliminarGalleta(id);
                        }
                        refrescarDatos();
                        actualizarTabla(administrador.getProductos());
                        JOptionPane.showMessageDialog(frame, "Producto eliminado con éxito.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error al eliminar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Seleccione un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Método para mostrar un diálogo para agregar o editar un producto.
    // Si productoExistente es null, se agrega un nuevo producto; de lo contrario, se edita.
    // Se declaran throws para CostoInvalidoException y ValorNegativoException para que los catch innecesarios se eliminen.
    private Producto mostrarDialogoProducto(Producto productoExistente) throws CostoInvalidoException, ValorNegativoException {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField txtNombre = new JTextField();
        JTextField txtCantidad = new JTextField();
        JTextField txtPrecioVenta = new JTextField();
        JTextField txtCostoProduccion = new JTextField();

        String[] tipos = {"Pan", "Galleta"};
        JComboBox<String> cbTipo = new JComboBox<>(tipos);
        JCheckBox chkCaracteristica = new JCheckBox();
        JLabel lblCaracteristica = new JLabel("Característica:");

        if (productoExistente != null) {
            txtNombre.setText(productoExistente.getNombre());
            txtCantidad.setText(String.valueOf(productoExistente.getCantidad()));
            txtPrecioVenta.setText(String.valueOf(productoExistente.getPrecioVenta()));
            txtCostoProduccion.setText(String.valueOf(productoExistente.getCostoProduccion()));
            if (productoExistente instanceof Pan) {
                cbTipo.setSelectedItem("Pan");
                chkCaracteristica.setSelected(((Pan) productoExistente).isTieneQueso());
                lblCaracteristica.setText("Tiene Queso:");
            } else if (productoExistente instanceof Galleta) {
                cbTipo.setSelectedItem("Galleta");
                chkCaracteristica.setSelected(((Galleta) productoExistente).isTieneChispasChocolate());
                lblCaracteristica.setText("Tiene Chispas:");
            }
        }

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);
        panel.add(new JLabel("Precio Venta:"));
        panel.add(txtPrecioVenta);
        panel.add(new JLabel("Costo Producción:"));
        panel.add(txtCostoProduccion);
        panel.add(new JLabel("Tipo de Producto:"));
        panel.add(cbTipo);
        panel.add(lblCaracteristica);
        panel.add(chkCaracteristica);

        cbTipo.addActionListener((ActionEvent e) -> {
            String seleccionado = (String) cbTipo.getSelectedItem();
            if ("Pan".equals(seleccionado)) {
                lblCaracteristica.setText("Tiene Queso:");
            } else {
                lblCaracteristica.setText("Tiene Chispas:");
            }
        });

        int result = JOptionPane.showConfirmDialog(frame, panel,
                productoExistente == null ? "Agregar Producto" : "Editar Producto",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText();
                int cantidad = Integer.parseInt(txtCantidad.getText());
                double precioVenta = Double.parseDouble(txtPrecioVenta.getText());
                double costoProduccion = Double.parseDouble(txtCostoProduccion.getText());
                String tipo = (String) cbTipo.getSelectedItem();
                boolean caracteristica = chkCaracteristica.isSelected();

                if ("Pan".equals(tipo)) {
                    return new Pan(nombre, precioVenta, costoProduccion, cantidad, caracteristica);
                } else {
                    return new Galleta(nombre, precioVenta, costoProduccion, cantidad, caracteristica);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error en el formato de número. Verifique los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        return null;
    }

    public void iniciar() {
        frame.setVisible(true);
    }
}
