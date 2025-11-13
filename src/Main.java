import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;

/**
 * Main.java
 * Punto de entrada. Crea productos, clientes, cola compartida y cajeras (hilos).
 * Ejecuta la simulación y muestra resultados finales.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        // Parámetros configurables
        final int NUM_CAJERAS = 3;
        final int NUM_CLIENTES = 8;

        // Cola donde los clientes esperan a ser atendidos
        BlockingQueue<Cliente> colaClientes = new LinkedBlockingQueue<>();

        // Contadores atómicos para la estadística global
        AtomicLong tiempoTotalGlobalMs = new AtomicLong(0);
        DoubleAdder montoTotalGlobal = new DoubleAdder();

        // Crear lista de productos disponibles
        List<Producto> catalogo = Arrays.asList(
                new Producto("Arroz 1kg", 4.20, 500),
                new Producto("Leche 1L", 1.80, 300),
                new Producto("Pan", 0.90, 200),
                new Producto("Huevos (12)", 3.50, 400),
                new Producto("Pollo 1kg", 6.50, 800),
                new Producto("Café 250g", 5.00, 600),
                new Producto("Aceite 1L", 7.20, 700)
        );

        // Generar clientes con listas de productos aleatorias
        Random rnd = new Random();
        for (int i = 1; i <= NUM_CLIENTES; i++) {
            int numProductos = rnd.nextInt(4) + 1; // entre 1 y 4 productos
            List<Producto> compra = new ArrayList<>();
            for (int j = 0; j < numProductos; j++) {
                Producto p = catalogo.get(rnd.nextInt(catalogo.size()));
                compra.add(p);
            }
            Cliente cliente = new Cliente("Cliente-" + i, compra);
            colaClientes.put(cliente);
            System.out.printf("[Main] Encolado %s: %d productos, total=%.2f $%n",
                    cliente.getNombre(), compra.size(), cliente.totalCompra());
        }

        // Crear y arrancar hilos (cajeras)
        List<Thread> hilosCajeras = new ArrayList<>();
        for (int c = 1; c <= NUM_CAJERAS; c++) {
            Cajera cajera = new Cajera("Cajera-" + c, colaClientes, tiempoTotalGlobalMs, montoTotalGlobal);
            Thread t = new Thread(cajera);
            t.start();
            hilosCajeras.add(t);
        }

        // Esperar un poco para que las cajeras atiendan los clientes
        // (NO es necesario pero da tiempo para que comiencen)
        Thread.sleep(1000);

        // Tras encolar TODOS los clientes, encolamos un "POISON PILL" para indicar fin:
        // Encolamos un único cliente especial "FIN" — al tomarlo, cada cajera lo volverá a poner
        // y terminará, y la re-encolará para que las otras cajeras también lo reciban.
        Cliente fin = new Cliente("FIN", Collections.emptyList());
        colaClientes.put(fin);

        // Esperar a que terminen todas las cajeras
        for (Thread t : hilosCajeras) {
            t.join();
        }

        // Mostrar resultados finales
        System.out.println("====================================");
        System.out.printf("Clientes atendidos (estimado): %d%n", NUM_CLIENTES);
        System.out.printf("Tiempo total de cobro (suma de tiempos por cliente): %d ms (%.2f s)%n",
                tiempoTotalGlobalMs.get(), tiempoTotalGlobalMs.get() / 1000.0);
        System.out.printf("Monto total facturado: %.2f $%n", montoTotalGlobal.doubleValue());
        System.out.println("====================================");
        System.out.println("Fin de la simulación.");
    }
}