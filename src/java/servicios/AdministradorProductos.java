package src.java.servicios;

import src.java.modelo.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdministradorProductos {
    // Asociación: AdministradorProductos mantiene una lista de Productos
    private List<Producto> productos;

    public AdministradorProductos() {
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto p) {
        productos.add(p);
    }

    public void eliminarProducto(Producto p) {
        productos.remove(p);
    }

    public List<Producto> filtrarPorPrecio(double precioMinimo) {
        return productos.stream()
                .filter(p -> p.getPrecioVenta() >= precioMinimo)
                .collect(Collectors.toList());
    }

    public List<Producto> filtrarPorNombre(String nombre) {
        return productos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Producto> filtrarPorCantidad(int cantidadMinima) {
        return productos.stream()
                .filter(p -> p.getCantidad() >= cantidadMinima)
                .collect(Collectors.toList());
    }

    public List<Producto> getProductos() {
        return productos;
    }
}
