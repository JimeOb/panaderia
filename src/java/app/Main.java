package src.java.app;

import src.java.excepciones.CostoInvalidoException;
import src.java.excepciones.ValorNegativoException;
import src.java.modelo.Galleta;
import src.java.modelo.Pan;
import src.java.servicios.AdministradorProductos;
import src.java.ui.interfazGrafica;

public class Main {
    public static void main(String[] args) {
        AdministradorProductos administrador = new AdministradorProductos();

        try {
            // Crear productos de ejemplo
            Pan panConQueso = new Pan("Pan Integral", 2.50, 1.50, 20, true);
            Galleta galletaChoco = new Galleta("Galleta de Avena", 1.20, 0.80, 50, true);

            // Agregar productos al administrador
            administrador.agregarProducto(panConQueso);
            administrador.agregarProducto(galletaChoco);
        } catch (CostoInvalidoException | ValorNegativoException e) {
            System.err.println("Error al crear producto: " + e.getMessage());
        }

        // Inicializar la interfaz gráfica
        interfazGrafica interfaz = new interfazGrafica(administrador);
        interfaz.iniciar();
    }
}
