package src.java.modelo;

import src.java.excepciones.CostoInvalidoException;
import src.java.excepciones.ValorNegativoException;

public class Galleta extends Producto {
    private boolean tieneChispasChocolate;

    public Galleta(String nombre, double precioVenta, double costoProduccion, int cantidad,
            boolean tieneChispasChocolate)
            throws CostoInvalidoException, ValorNegativoException {
        super(nombre, precioVenta, costoProduccion, cantidad);
        this.tieneChispasChocolate = tieneChispasChocolate;
    }

    public boolean isTieneChispasChocolate() {
        return tieneChispasChocolate;
    }

    @Override
    public String toString() {
        return "Galleta{" +
                "nombre='" + nombre + '\'' +
                ", precioVenta=" + precioVenta +
                ", costoProduccion=" + costoProduccion +
                ", cantidad=" + cantidad +
                ", tieneChispasChocolate=" + tieneChispasChocolate +
                '}';
    }
}
