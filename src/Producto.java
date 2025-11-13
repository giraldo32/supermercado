
/**producto
 * Producto.java
 * Representa un producto con nombre, precio y tiempo de procesamiento (ms).
 */
public class Producto {
    private final String nombre;
    private final double precio;
    private final long tiempoProcesamientoMs;

    public Producto(String nombre, double precio, long tiempoProcesamientoMs) {
        this.nombre = nombre;
        this.precio = precio;
        this.tiempoProcesamientoMs = tiempoProcesamientoMs;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public long getTiempoProcesamientoMs() {
        return tiempoProcesamientoMs;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f $) - %d ms", nombre, precio, tiempoProcesamientoMs);
    }
}