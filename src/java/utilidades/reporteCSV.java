package src.java.utilidades;

import src.java.modelo.Producto;
import src.java.modelo.Pan;
import src.java.modelo.Galleta;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class reporteCSV {
    public static void generarReporteCSV(List<Producto> productos, String rutaArchivo) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            // Encabezados del CSV
            writer.append("Nombre,PrecioVenta,CostoProduccion,Cantidad,Detalle\n");
            for (Producto p : productos) {
                String detalle = "";
                if (p instanceof Pan) {
                    detalle = "Tiene queso: " + ((Pan) p).isTieneQueso();
                } else if (p instanceof Galleta) {
                    detalle = "Tiene chispas de chocolate: " + ((Galleta) p).isTieneChispasChocolate();
                }
                writer.append(String.format("%s,%.2f,%.2f,%d,%s\n",
                        p.getNombre(), p.getPrecioVenta(), p.getCostoProduccion(), p.getCantidad(), detalle));
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error al generar el reporte CSV: " + e.getMessage());
        }
    }
}
