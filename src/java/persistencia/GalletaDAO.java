package src.java.persistencia;

import src.java.modelo.Galleta;
import src.java.excepciones.CostoInvalidoException;
import src.java.excepciones.ValorNegativoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GalletaDAO {
    private Connection connection;

    public GalletaDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // Inserta una Galleta en la base de datos
    public void insertarGalleta(Galleta galleta) throws SQLException {
        String sql = "INSERT INTO producto (cantidad, nombre, precioVenta, costoProduccion, queso, chocolate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, galleta.getCantidad());
            stmt.setString(2, galleta.getNombre());
            stmt.setInt(3, (int) galleta.getPrecioVenta());
            stmt.setInt(4, (int) galleta.getCostoProduccion());
            stmt.setString(5, ""); // Para Galleta, el campo queso se establece vacío.
            stmt.setString(6, String.valueOf(galleta.isTieneChispasChocolate()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                galleta.setIdProducto(rs.getInt(1));
            }
        }
    }

    // Lista todas las Galletas almacenadas en la base de datos
    public List<Galleta> listarGalletas() throws SQLException, CostoInvalidoException, ValorNegativoException {
        List<Galleta> galletas = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE chocolate IS NOT NULL AND TRIM(chocolate) <> ''";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("idProducto");
                String nombre = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                int precioVenta = rs.getInt("precioVenta");
                int costoProduccion = rs.getInt("costoProduccion");
                String chocolateStr = rs.getString("chocolate");
                boolean tieneChispasChocolate = Boolean.parseBoolean(chocolateStr);
                Galleta galleta = new Galleta(nombre, precioVenta, costoProduccion, cantidad, tieneChispasChocolate);
                galleta.setIdProducto(id);
                galletas.add(galleta);
            }
        }
        return galletas;
    }
    
    // Actualiza (edita) una Galleta existente en la base de datos
    public void actualizarGalleta(Galleta galleta) throws SQLException {
        String sql = "UPDATE producto SET cantidad = ?, nombre = ?, precioVenta = ?, costoProduccion = ?, queso = ?, chocolate = ? WHERE idProducto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, galleta.getCantidad());
            stmt.setString(2, galleta.getNombre());
            stmt.setInt(3, (int) galleta.getPrecioVenta());
            stmt.setInt(4, (int) galleta.getCostoProduccion());
            stmt.setString(5, ""); // Campo 'queso' se establece vacío para Galleta.
            stmt.setString(6, String.valueOf(galleta.isTieneChispasChocolate()));
            stmt.setInt(7, galleta.getIdProducto());
            stmt.executeUpdate();
        }
    }

    // Elimina una Galleta de la base de datos por su idProducto
    public void eliminarGalleta(int idProducto) throws SQLException {
        String sql = "DELETE FROM producto WHERE idProducto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            stmt.executeUpdate();
        }
    }
}
