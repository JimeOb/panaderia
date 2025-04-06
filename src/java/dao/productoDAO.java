package src.java.dao;

import src.java.modelo.Producto;
import src.java.modelo.Pan;
import src.java.persistencia.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class productoDAO {

    public void saveProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, precioVenta, costoProduccion, cantidad) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecioVenta());
            ps.setDouble(3, producto.getCostoProduccion());
            ps.setInt(4, producto.getCantidad());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar el producto: " + e.getMessage());
        }
    }

    public void deleteProducto(Producto producto) {
        String sql = "DELETE FROM productos WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar el producto: " + e.getMessage());
        }
    }

    public void updateProducto(Producto producto) {
        String sql = "UPDATE productos SET precioVenta = ?, costoProduccion = ?, cantidad = ? WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, producto.getPrecioVenta());
            ps.setDouble(2, producto.getCostoProduccion());
            ps.setInt(3, producto.getCantidad());
            ps.setString(4, producto.getNombre());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar el producto: " + e.getMessage());
        }
    }

    public List<Producto> getAllProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Para fines de ejemplo se crea un objeto Pan. En una implementación real,
                // se debería distinguir el tipo (Pan, Galleta, etc.) usando una columna de
                // discriminación.
                String nombre = rs.getString("nombre");
                double precioVenta = rs.getDouble("precioVenta");
                double costoProduccion = rs.getDouble("costoProduccion");
                int cantidad = rs.getInt("cantidad");
                Producto producto = new Pan(nombre, precioVenta, costoProduccion, cantidad, false);
                productos.add(producto);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
        return productos;
    }
}