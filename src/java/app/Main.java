package src.java.app;

import src.java.excepciones.CostoInvalidoException;
import src.java.excepciones.ValorNegativoException;
import src.java.modelo.Pan;
import src.java.modelo.Galleta;
import src.java.persistencia.PanDAO;
import src.java.persistencia.GalletaDAO;
import src.java.servicios.AdministradorProductos;
import src.java.ui.interfazGrafica;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AdministradorProductos administrador = new AdministradorProductos();

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

        } catch (CostoInvalidoException | ValorNegativoException | SQLException e) {
            e.printStackTrace();
        }

        interfazGrafica interfaz = new interfazGrafica(administrador);
        interfaz.iniciar();
    }
}






