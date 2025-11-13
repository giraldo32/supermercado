import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;

/**cajera
 * Cajera.java
 * Cada instancia procesa clientes tomándolos de una cola compartida.
 * Actualiza contadores atómicos para tiempo total y monto total.
 */
public class Cajera implements Runnable {
    private final String nombre;
    private final BlockingQueue<Cliente> colaClientes;
    private final AtomicLong tiempoTotalGlobalMs; // suma de todos los tiempos de cobro (ms)
    private final DoubleAdder montoTotalGlobal;   // suma de todos los totales $/ventas

    public Cajera(String nombre,
                  BlockingQueue<Cliente> colaClientes,
                  AtomicLong tiempoTotalGlobalMs,
                  DoubleAdder montoTotalGlobal) {
        this.nombre = nombre;
        this.colaClientes = colaClientes;
        this.tiempoTotalGlobalMs = tiempoTotalGlobalMs;
        this.montoTotalGlobal = montoTotalGlobal;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Cliente cliente = colaClientes.take(); // bloquea si no hay clientes
                // Señal de terminación — cliente con nombre "FIN"
                if (cliente.getNombre().equals("FIN")) {
                    // Re-enqueue para que otras cajeras también reciban la señal
                    colaClientes.put(cliente);
                    System.out.printf("[%s] recibe señal de FIN y termina.%n", nombre);
                    break;
                }

                System.out.printf("[%s] Inicia cobro de %s%n", nombre, cliente.getNombre());
                long tInicio = System.currentTimeMillis();

                // Procesar cada producto del cliente
                for (Producto p : cliente.getProductos()) {
                    System.out.printf("  [%s] Procesando producto: %s — precio: %.2f — tiempo: %d ms%n",
                            nombre, p.getNombre(), p.getPrecio(), p.getTiempoProcesamientoMs());
                    try {
                        Thread.sleep(p.getTiempoProcesamientoMs());
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        System.out.printf("[%s] Interrumpido mientras procesaba.%n", nombre);
                    }
                }

                long tFin = System.currentTimeMillis();
                long tiempoClienteMs = tFin - tInicio;
                double monto = cliente.totalCompra();

                // Actualizar contadores globales
                tiempoTotalGlobalMs.addAndGet(tiempoClienteMs);
                montoTotalGlobal.add(monto);

                System.out.printf("[%s] Finaliza cobro de %s — Tiempo: %d ms — Total compra: %.2f $%n",
                        nombre, cliente.getNombre(), tiempoClienteMs, monto);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.printf("[%s] Hilo interrumpido y finaliza.%n", nombre);
        }
    }
}