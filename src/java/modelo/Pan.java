package src.java.modelo;

import src.java.excepciones.CostoInvalidoException;
import src.java.excepciones.ValorNegativoException;

public class Pan extends Producto {
    private boolean tieneQueso;

    public Pan(String nombre, double precioVenta, double costoProduccion, int cantidad, boolean tieneQueso)
            throws CostoInvalidoException, ValorNegativoException {
        super(nombre, precioVenta, costoProduccion, cantidad);
        this.tieneQueso = tieneQueso;
    }

    public boolean isTieneQueso() {
        return tieneQueso;
    }

    @Override
    public String toString() {
        return "Pan{" +
                "idProducto=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", cantidad=" + cantidad +
                ", precioVenta=" + precioVenta +
                ", costoProduccion=" + costoProduccion +
                ", tieneQueso=" + tieneQueso +
                '}';
    }
}
