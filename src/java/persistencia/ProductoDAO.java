package src.java.persistencia;

import src.java.modelo.Producto;
import src.java.modelo.Pan;
import src.java.modelo.Galleta;
import src.java.excepciones.CostoInvalidoException;
import src.java.excepciones.ValorNegativoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private Connection connection;

    public ProductoDAO() throws SQLException {
        // Obtiene la conexión mediante DatabaseConnection
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // Método para insertar un producto (Pan o Galleta)
    public void insertarProducto(Producto producto) throws SQLException {
        String sql = "INSERT INTO producto (cantidad, nombre, precioVenta, costoProduccion, queso, chocolate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, producto.getCantidad());
            stmt.setString(2, producto.getNombre());
            // Se convierten a INT; en la BD están definidos como INT
            stmt.setInt(3, (int) producto.getPrecioVenta());
            stmt.setInt(4, (int) producto.getCostoProduccion());

            if (producto instanceof Pan) {
                Pan pan = (Pan) producto;
                // Para Pan se utiliza el campo "queso" y se asigna un valor por defecto en "chocolate"
                stmt.setString(5, String.valueOf(pan.isTieneQueso()));
                stmt.setString(6, "");
            } else if (producto instanceof Galleta) {
                Galleta galleta = (Galleta) producto;
                // Para Galleta se utiliza el campo "chocolate" y se asigna un valor por defecto en "queso"
                stmt.setString(5, "");
                stmt.setString(6, String.valueOf(galleta.isTieneChispasChocolate()));
            } else {
                stmt.setString(5, "");
                stmt.setString(6, "");
            }

            stmt.executeUpdate();

            // Recupera el id generado y lo asigna al objeto
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                producto.setIdProducto(rs.getInt(1));
            }
        }
    }

    // Método para listar los productos desde la base de datos
    public List<Producto> listarProductos() throws SQLException, CostoInvalidoException, ValorNegativoException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("idProducto");
                String nombre = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                int precioVenta = rs.getInt("precioVenta");
                int costoProduccion = rs.getInt("costoProduccion");
                String queso = rs.getString("queso");
                String chocolate = rs.getString("chocolate");

                Producto producto = null;
                // Si el campo "queso" tiene un valor, asumimos que es un Pan
                if (queso != null && !queso.trim().isEmpty()) {
                    boolean tieneQueso = Boolean.parseBoolean(queso);
                    producto = new Pan(nombre, precioVenta, costoProduccion, cantidad, tieneQueso);
                }
                // Si el campo "chocolate" tiene un valor, asumimos que es una Galleta
                else if (chocolate != null && !chocolate.trim().isEmpty()) {
                    boolean tieneChispasChocolate = Boolean.parseBoolean(chocolate);
                    producto = new Galleta(nombre, precioVenta, costoProduccion, cantidad, tieneChispasChocolate);
                }
                if (producto != null) {
                    producto.setIdProducto(id);
                    productos.add(producto);
                }
            }
        }
        return productos;
    }
    
    // Puedes agregar métodos adicionales para actualizar o eliminar productos
}


