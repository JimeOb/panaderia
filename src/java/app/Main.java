package src.java.app;

import src.java.excepciones.CostoInvalidoException;
import src.java.excepciones.ValorNegativoException;
import src.java.modelo.Pan;
import src.java.modelo.Galleta;
import src.java.persistencia.ProductoDAO;
import src.java.servicios.AdministradorProductos;
import src.java.ui.interfazGrafica;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Crear el administrador de productos en memoria
        AdministradorProductos administrador = new AdministradorProductos();

        try {
            // Crear productos de ejemplo e insertarlos en la base de datos
            Pan pan1 = new Pan("Pan Integral", 250, 150, 20, true);
            Galleta galleta1 = new Galleta("Galleta de Avena", 120, 80, 50, true);

            // Insertar productos en la base de datos utilizando ProductoDAO
            ProductoDAO productoDAO = new ProductoDAO();
            productoDAO.insertarProducto(pan1);
            productoDAO.insertarProducto(galleta1);

            // Consultar la base de datos y actualizar el administrador
            List productosBD = productoDAO.listarProductos();
            // Limpiar la lista actual (si deseas solo mostrar datos de la BD)
            administrador.getProductos().clear();
            // Agregar los registros obtenidos de la base de datos al administrador
            for (Object p : productosBD) {
                administrador.agregarProducto((src.java.modelo.Producto) p);
            }

        } catch (CostoInvalidoException | ValorNegativoException | SQLException e) {
            e.printStackTrace();
        }

        // Inicializar la interfaz gráfica con el administrador actualizado
        interfazGrafica interfaz = new interfazGrafica(administrador);
        interfaz.iniciar();
    }
}




