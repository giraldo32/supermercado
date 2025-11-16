# 🛒 Sistema de Simulación de Cobro - Supermercado

## 📋 Descripción

Aplicación Java que simula el proceso de cobro en un supermercado utilizando **concurrencia y programación con hilos**. El sistema permite gestionar múltiples cajeras que atienden clientes simultáneamente, procesando sus compras y calculando tiempos y costos totales.

## ✨ Características

### Interfaz Gráfica Profesional
- **Gestión Dinámica de Productos**: Agregar, modificar y eliminar productos del catálogo
- **Gestión de Clientes**: Crear clientes con sus listas de compras personalizadas
- **Configuración de Cajeras**: Ajustar el número de cajeras (hilos) que atenderán simultáneamente
- **Monitoreo en Tiempo Real**: Visualización del proceso de cobro con estadísticas actualizadas
- **Diseño Moderno**: Interfaz elegante con colores profesionales y efectos visuales

### Funcionalidades Principales
- ✅ Simulación de cobro concurrente con múltiples cajeras (hilos)
- ✅ Procesamiento cliente por cliente con productos individuales
- ✅ Cálculo automático de tiempos de procesamiento
- ✅ Estadísticas en tiempo real: clientes atendidos, tiempo total, monto facturado
- ✅ Generación aleatoria de clientes para pruebas rápidas
- ✅ Logs detallados de cada transacción
- ✅ Barra de progreso visual

## 🚀 Cómo Ejecutar

### Opción 1: Interfaz Gráfica (Recomendado)

```cmd
javac -d bin src/*.java
java -cp bin Main
```

### Opción 2: Modo Consola

```cmd
javac -d bin src/*.java
java -cp bin Main consola
```

## 📖 Guía de Uso - Interfaz Gráfica

### 1. Gestionar Catálogo de Productos
- Ve a la pestaña **"📦 Catálogo de Productos"**
- Haz clic en **"➕ Agregar Producto"**
- Ingresa:
  - Nombre del producto
  - Precio en dólares
  - Tiempo de procesamiento en milisegundos
- El sistema incluye 7 productos por defecto (arroz, leche, pan, etc.)

### 2. Agregar Clientes
- Ve a la pestaña **"👥 Clientes y Compras"**
- **Opción A - Manual**: 
  - Haz clic en **"➕ Agregar Cliente"**
  - Ingresa el nombre del cliente
  - Selecciona los productos que comprará
  
- **Opción B - Automático**: 
  - Haz clic en **"🎲 Generar Aleatorio"**
  - El sistema crea un cliente con productos aleatorios

### 3. Configurar Cajeras
- Ve a la pestaña **"⚙️ Configuración"**
- Ajusta el número de cajeras (hilos concurrentes) entre 1 y 10
- Más cajeras = mayor paralelismo en la atención

### 4. Iniciar Simulación
- Haz clic en **"▶️ Iniciar Simulación"**
- Observa el panel de resultados en tiempo real
- Las estadísticas se actualizan automáticamente:
  - **Clientes**: Número de clientes atendidos
  - **Tiempo**: Tiempo total acumulado
  - **Total**: Monto facturado total

### 5. Visualizar Resultados
- El panel de resultados muestra:
  - Cada cliente encolado
  - Proceso de cobro detallado por cajera
  - Productos procesados con sus precios y tiempos
  - Resumen final con estadísticas completas

## 🏗️ Estructura del Proyecto

```
supermercado/
├── src/
│   ├── Main.java                    # Punto de entrada (consola o GUI)
│   ├── InterfazSupermercado.java    # Interfaz gráfica completa
│   ├── Cajera.java                  # Clase que representa una cajera (Runnable)
│   ├── Cliente.java                 # Clase que representa un cliente
│   └── Producto.java                # Clase que representa un producto
├── bin/                             # Archivos compilados (.class)
└── README.md                        # Documentación
```

## 🧵 Conceptos de Concurrencia Aplicados

### 1. **Hilos (Threads)**
- Cada cajera es un hilo independiente (`Thread`)
- Permite el procesamiento simultáneo de múltiples clientes

### 2. **BlockingQueue**
- Cola compartida donde los clientes esperan ser atendidos
- Gestión automática de sincronización entre hilos
- Método `take()` bloquea hasta que haya clientes disponibles

### 3. **AtomicLong y DoubleAdder**
- Variables atómicas para evitar condiciones de carrera
- Acumulación segura de estadísticas desde múltiples hilos

### 4. **Patrón Poison Pill**
- Cliente especial "FIN" para señalizar terminación
- Permite detener todos los hilos de forma ordenada

## 💡 Ejemplo de Flujo de Ejecución

```
1. Usuario agrega productos al catálogo:
   - Arroz 1kg: $4.20, 500ms
   - Leche 1L: $1.80, 300ms
   
2. Usuario crea clientes:
   - Cliente-1: [Arroz, Leche, Pan] → Total: $6.90
   - Cliente-2: [Pollo, Café] → Total: $11.50
   
3. Usuario configura 3 cajeras y presiona "Iniciar Simulación"

4. Sistema ejecuta:
   [Cajera-1] → Atiende Cliente-1 (tiempo: 1200ms)
   [Cajera-2] → Atiende Cliente-2 (tiempo: 1400ms)
   [Cajera-3] → Espera más clientes...
   
5. Resultados finales:
   - Clientes atendidos: 2
   - Tiempo total: 2600ms (2.6s)
   - Monto facturado: $18.40
```

## 🎨 Características de la Interfaz

### Panel de Catálogo
- Tabla con productos, precios y tiempos
- Botones para agregar/eliminar productos
- Validación de datos de entrada

### Panel de Clientes
- Tabla con resumen de compras por cliente
- Selección múltiple de productos por cliente
- Generador aleatorio para pruebas rápidas

### Panel de Configuración
- Spinner para seleccionar número de cajeras
- Instrucciones de uso
- Configuración fácil e intuitiva

### Panel de Resultados
- Área de texto con logs en tiempo real
- Estilo "terminal" con fondo oscuro y texto verde
- Auto-scroll para seguir el proceso
- Barra de progreso visual

### Estadísticas en Vivo
- Header con métricas actualizadas constantemente
- Colores profesionales y diseño moderno
- Botón de limpiar para reiniciar simulaciones

## 🔧 Requisitos Técnicos

- **Java**: JDK 8 o superior
- **Sistema Operativo**: Windows, Linux, macOS
- **Bibliotecas**: Java Swing (incluida en JDK)

## 👨‍💻 Desarrollo

**Institución**: IUDigital de Antioquia  
**Departamento**: Tecnología en Desarrollo de Software  
**Tema**: Concurrencia y Programación con Hilos en Java

## 📝 Notas Adicionales

- La simulación es **determinista** pero con datos configurables
- Los tiempos de procesamiento son simulados con `Thread.sleep()`
- El sistema es **thread-safe** utilizando estructuras concurrentes
- La interfaz usa `SwingUtilities.invokeLater()` para actualizaciones seguras de UI

## 🎯 Objetivos Cumplidos

✅ Aplicación de conceptos de concurrencia en Java  
✅ Uso de hilos para procesos simultáneos  
✅ Gestión de recursos compartidos (cola de clientes)  
✅ Sincronización segura con estructuras atómicas  
✅ Interfaz gráfica profesional y elegante  
✅ Carga dinámica de datos (sin hardcoding)  
✅ Monitoreo en tiempo real de la simulación  

---

**¡Disfruta de la simulación!** 🎉
