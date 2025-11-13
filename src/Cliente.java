import java.util.List;

/**cliente
 * Cliente.java
 * Representa un cliente con una lista de productos.
 * Si es un cliente "POISON_PILL" para terminar, su lista de productos puede ser vacía
 * y su nombre puede ser "FIN".
 */
public class Cliente {
    private final String nombre;
    private final List<Producto> productos;

    public Cliente(String nombre, List<Producto> productos) {
        this.nombre = nombre;
        this.productos = productos;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public double totalCompra() {
        return productos.stream().mapToDouble(Producto::getPrecio).sum();
    }

    @Override
    public String toString() {
        return String.format("Cliente %s - %d productos - Total: %.2f $",
                nombre, productos.size(), totalCompra());
    }
}