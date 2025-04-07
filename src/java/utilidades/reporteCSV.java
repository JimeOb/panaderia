package src.java.utilidades;

import src.java.modelo.Producto;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class reporteCSV {
    public static void generarReporteCSV(List<Producto> productos, String nombreArchivo) {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            // Escribe la cabecera del CSV
            writer.append("ID,Nombre,Cantidad,PrecioVenta,CostoProduccion,Caracteristica\n");
            
            // Recorre la lista de productos y escribe cada uno en el CSV
            for (Producto p : productos) {
                writer.append(String.valueOf(p.getIdProducto())).append(",");
                writer.append(p.getNombre()).append(",");
                writer.append(String.valueOf(p.getCantidad())).append(",");
                writer.append(String.valueOf(p.getPrecioVenta())).append(",");
                writer.append(String.valueOf(p.getCostoProduccion())).append(",");
                
                // Según el tipo de producto, agrega la característica correspondiente
                if (p instanceof src.java.modelo.Pan) {
                    src.java.modelo.Pan pan = (src.java.modelo.Pan) p;
                    writer.append("tieneQueso=").append(String.valueOf(pan.isTieneQueso()));
                } else if (p instanceof src.java.modelo.Galleta) {
                    src.java.modelo.Galleta galleta = (src.java.modelo.Galleta) p;
                    writer.append("tieneChispasChocolate=").append(String.valueOf(galleta.isTieneChispasChocolate()));
                } else {
                    writer.append("");
                }
                writer.append("\n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

