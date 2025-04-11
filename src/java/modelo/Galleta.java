package src.java.modelo;

import src.java.excepciones.CostoInvalidoException;
import src.java.excepciones.ValorNegativoException;

public class Galleta extends Producto {
    private boolean tieneChispasChocolate;

    public Galleta(String nombre, double precioVenta, double costoProduccion, int cantidad, boolean tieneChispasChocolate)
            throws CostoInvalidoException, ValorNegativoException {
        super(nombre, precioVenta, costoProduccion, cantidad);
        
        if (costoProduccion > precioVenta) {
            throw new CostoInvalidoException("El costo de producción (" + costoProduccion +
                    ") no puede ser mayor que el precio de venta (" + precioVenta + ").");
        }
        
        if (precioVenta < 0 || costoProduccion < 0 || cantidad < 0) {
            throw new ValorNegativoException("Los valores de precio, costo o cantidad no pueden ser negativos.");
        }
        
        this.tieneChispasChocolate = tieneChispasChocolate;
    }

    public boolean isTieneChispasChocolate() {
        return tieneChispasChocolate;
    }

    @Override
    public String toString() {
        return "Galleta{" +
                "idProducto=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", cantidad=" + cantidad +
                ", precioVenta=" + precioVenta +
                ", costoProduccion=" + costoProduccion +
                ", tieneChispasChocolate=" + tieneChispasChocolate +
                '}';
    }
}
