package src.java.ui;

import src.java.modelo.Producto;
import src.java.servicios.AdministradorProductos;
import src.java.utilidades.reporteCSV;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class interfazGrafica {
    private AdministradorProductos administrador;
    private JFrame frame;
    private JTextArea textArea;

    public interfazGrafica(AdministradorProductos administrador) {
        this.administrador = administrador;
        crearInterfaz();
    }

    private void crearInterfaz() {
        frame = new JFrame("Gestión de Productos - Panadería");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Área de texto para mostrar productos
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnMostrar = new JButton("Mostrar Productos");
        JButton btnGenerarCSV = new JButton("Generar Reporte CSV");

        btnMostrar.addActionListener((ActionEvent e) -> mostrarProductos(administrador.getProductos()));
        btnGenerarCSV.addActionListener((ActionEvent e) -> {
            reporteCSV.generarReporteCSV(administrador.getProductos(), "reporte_productos.csv");
            JOptionPane.showMessageDialog(frame, "Reporte CSV generado.");
        });

        panelBotones.add(btnMostrar);
        panelBotones.add(btnGenerarCSV);

        frame.add(panelBotones, BorderLayout.SOUTH);
    }

    public void iniciar() {
        frame.setVisible(true);
    }

    private void mostrarProductos(List<Producto> productos) {
        textArea.setText("");
        for (Producto p : productos) {
            textArea.append(p.toString() + "\n");
        }
    }
}