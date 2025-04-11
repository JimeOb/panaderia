package src.java.modelo;

import src.java.excepciones.CostoInvalidoException;
import src.java.excepciones.ValorNegativoException;

public abstract class Producto {
    protected int idProducto;
    protected String nombre;
    protected double precioVenta;
    protected double costoProduccion;
    protected int cantidad;

    public Producto(String nombre, double precioVenta, double costoProduccion, int cantidad)
            throws CostoInvalidoException, ValorNegativoException {
        if (precioVenta < 0 || costoProduccion < 0 || cantidad < 0) {
            throw new ValorNegativoException("Los valores no pueden ser negativos.");
        }
        if (costoProduccion > precioVenta) {
            throw new CostoInvalidoException("El costo de producción no puede ser mayor que el precio de venta.");
        }
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.costoProduccion = costoProduccion;
        this.cantidad = cantidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public double getCostoProduccion() {
        return costoProduccion;
    }

    public int getCantidad() {
        return cantidad;
    }
}

