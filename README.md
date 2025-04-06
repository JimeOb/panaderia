# Proyecto Panadería

Este proyecto es un sistema de información para administrar las ventas y productos de una panadería ubicada frente a la embajada de Estados Unidos. La aplicación permite registrar y gestionar productos (pan y galletas), realizar validaciones de datos mediante excepciones personalizadas y filtrar la información por precio, nombre y cantidad. Además, cuenta con la capacidad de generar un reporte en formato CSV con todos los productos registrados.

## Características

- **Gestión de Productos:** Registro, edición y eliminación de productos.
- **Validaciones:** Se asegura que el costo de producción no supere el precio de venta y que los valores numéricos sean positivos, usando excepciones personalizadas.
- **Interfaz Gráfica:** Implementada con Swing para facilitar la interacción del usuario.
- **Persistencia:** Conexión a una base de datos MySQL para guardar y recuperar la información de los productos.
- **Reporte CSV:** Generación de un archivo CSV con los datos de los productos.

## Estructura del Proyecto

El proyecto se organiza en los siguientes paquetes:

- **app:** Contiene la clase principal (`Main.java`).
- **dao:** Acceso a datos (clase `ProductoDAO.java`).
- **excepciones:** Excepciones personalizadas (`CostoInvalidoException.java`, `ValorNegativoException.java`).
- **modelo:** Clases del dominio (abstracta `Producto.java`, y sus implementaciones `Pan.java` y `Galleta.java`).
- **persistencia:** Conexión a la base de datos (`DatabaseConnection.java`).
- **servicios:** Lógica de negocio (clase `AdministradorProductos.java`).
- **ui:** Interfaz gráfica (clase `InterfazGrafica.java`).
- **utilidades:** Herramientas auxiliares (clase `CSVReportGenerator.java`).


## Requisitos

- **Java:** JDK 11 o superior.
- **Base de Datos:** MySQL (u otro compatible) para la persistencia.
- **IDE:** Visual Studio Code con el Java Extension Pack instalado.




