package src.java.persistencia;

import src.java.modelo.Pan;
import src.java.excepciones.CostoInvalidoException;
import src.java.excepciones.ValorNegativoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanDAO {
    private Connection connection;

    public PanDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void insertarPan(Pan pan) throws SQLException {
        String sql = "INSERT INTO producto (cantidad, nombre, precioVenta, costoProduccion, queso, chocolate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pan.getCantidad());
            stmt.setString(2, pan.getNombre());
            stmt.setInt(3, (int) pan.getPrecioVenta());
            stmt.setInt(4, (int) pan.getCostoProduccion());
            stmt.setString(5, String.valueOf(pan.isTieneQueso()));
            stmt.setString(6, "");  // Campo "chocolate" vacío para Pan
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                pan.setIdProducto(rs.getInt(1));
            }
        }
    }

    public List<Pan> listarPanes() throws SQLException, CostoInvalidoException, ValorNegativoException {
        List<Pan> pans = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE queso IS NOT NULL AND TRIM(queso) <> ''";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("idProducto");
                String nombre = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                int precioVenta = rs.getInt("precioVenta");
                int costoProduccion = rs.getInt("costoProduccion");
                String quesoStr = rs.getString("queso");
                boolean tieneQueso = Boolean.parseBoolean(quesoStr);
                Pan pan = new Pan(nombre, precioVenta, costoProduccion, cantidad, tieneQueso);
                pan.setIdProducto(id);
                pans.add(pan);
            }
        }
        return pans;
    }

    public void actualizarPan(Pan pan) throws SQLException {
        String sql = "UPDATE producto SET cantidad = ?, nombre = ?, precioVenta = ?, costoProduccion = ?, queso = ?, chocolate = ? WHERE idProducto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pan.getCantidad());
            stmt.setString(2, pan.getNombre());
            stmt.setInt(3, (int) pan.getPrecioVenta());
            stmt.setInt(4, (int) pan.getCostoProduccion());
            stmt.setString(5, String.valueOf(pan.isTieneQueso()));
            stmt.setString(6, ""); // Para Pan, dejamos vacío el campo chocolate
            stmt.setInt(7, pan.getIdProducto());
            stmt.executeUpdate();
        }
    }

    public void eliminarPan(int idProducto) throws SQLException {
        String sql = "DELETE FROM producto WHERE idProducto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            stmt.executeUpdate();
        }
    }
}

