# 📚 GUÍA RÁPIDA DE USO - Sistema de Simulación de Supermercado

## 🚀 Inicio Rápido

### Paso 1: Compilar
```cmd
compilar.bat
```
O manualmente:
```cmd
javac -encoding UTF-8 -d bin src\*.java
```

### Paso 2: Ejecutar
**Interfaz Gráfica (Recomendado):**
```cmd
ejecutar.bat
```
O manualmente:
```cmd
java -cp bin Main
```

**Modo Consola:**
```cmd
ejecutar_consola.bat
```
O manualmente:
```cmd
java -cp bin Main consola
```

---

## 🎯 Flujo de Trabajo - Interfaz Gráfica

### 1️⃣ Gestionar Catálogo
- Abre la pestaña **"📦 Catálogo de Productos"**
- Productos por defecto ya están cargados
- Puedes agregar más productos con:
  - ➕ **Agregar Producto**: Añade productos personalizados
  - 🗑️ **Eliminar Producto**: Elimina productos seleccionados

**Datos necesarios por producto:**
- Nombre (ej: "Manzanas 1kg")
- Precio en $ (ej: 2.50)
- Tiempo de procesamiento en ms (ej: 400)

---

### 2️⃣ Crear Clientes
- Abre la pestaña **"👥 Clientes y Compras"**
- Tres formas de agregar clientes:

**Opción A - Manual:**
1. Click en ➕ **Agregar Cliente**
2. Ingresa nombre del cliente
3. Selecciona productos que comprará (checkboxes)
4. Click en **Aceptar**

**Opción B - Automático:**
1. Click en 🎲 **Generar Aleatorio**
2. Sistema crea cliente con 1-4 productos aleatorios

**Opción C - Múltiples:**
- Usa "Generar Aleatorio" varias veces para crear varios clientes rápidamente

---

### 3️⃣ Configurar Cajeras
- Abre la pestaña **"⚙️ Configuración"**
- Ajusta el número de cajeras (1-10)
- **Recomendado**: 3 cajeras para pruebas
- Más cajeras = Mayor concurrencia

---

### 4️⃣ Ejecutar Simulación
1. Click en **▶️ Iniciar Simulación**
2. Observa el panel de resultados:
   - Logs en tiempo real con cada operación
   - Estadísticas actualizándose (arriba)
   - Barra de progreso (abajo)

---

### 5️⃣ Interpretar Resultados

**Panel Superior (Estadísticas):**
```
Clientes: X    Tiempo: X.X s    Total: $X.XX
```

**Panel de Resultados:**
```
[Sistema] Encolado Cliente-1: 3 productos, total=$10.50
[Cajera-1] ► Inicia cobro de Cliente-1
  [Cajera-1] → Procesando: Arroz 1kg — Precio: $4.20 — Tiempo: 500 ms
  [Cajera-1] → Procesando: Leche 1L — Precio: $1.80 — Tiempo: 300 ms
  ...
[Cajera-1] ✓ Finaliza cobro de Cliente-1 — Tiempo: 1200 ms (1.20 s) — Total: $10.50
```

---

## 💡 Ejemplos de Uso

### Ejemplo 1: Simulación Básica (3 cajeras, 5 clientes)

1. **Preparación:**
   - Usa productos por defecto
   - Genera 5 clientes aleatorios
   - Configura 3 cajeras

2. **Ejecución:**
   - Presiona "Iniciar Simulación"
   - Observa cómo 3 cajeras atienden simultáneamente
   - Algunos clientes esperan en cola

3. **Resultado esperado:**
   - Tiempo total: ~3-5 segundos
   - Monto total: Variable según productos
   - 5 clientes atendidos

---

### Ejemplo 2: Prueba de Concurrencia (5 cajeras, 10 clientes)

1. **Preparación:**
   - Genera 10 clientes aleatorios
   - Configura 5 cajeras

2. **Observación:**
   - 5 clientes atendidos simultáneamente al inicio
   - Cajeras procesan productos en paralelo
   - Cola se vacía más rápido

3. **Comparación:**
   - Compara tiempo con 1 cajera vs 5 cajeras
   - Observa eficiencia de concurrencia

---

### Ejemplo 3: Cliente Personalizado

1. **Crear productos específicos:**
   ```
   Producto: TV 50"
   Precio: $450.00
   Tiempo: 2000 ms (procesamiento lento)
   ```

2. **Crear cliente:**
   ```
   Nombre: Cliente VIP
   Productos: [TV 50", Garantía Extendida, Cable HDMI]
   ```

3. **Ejecutar:**
   - Observa que este cliente toma más tiempo
   - Otras cajeras atienden a otros clientes mientras tanto

---

## 🎨 Características de la Interfaz

### Colores y Significados

**Header Azul:**
- Título de la aplicación
- Estadísticas en tiempo real

**Botones Verdes:**
- ➕ Agregar (Productos/Clientes)
- ▶️ Iniciar Simulación

**Botones Rojos:**
- 🗑️ Eliminar elementos

**Botones Azules:**
- 🎲 Generar aleatorio

**Botones Grises:**
- 🗑️ Limpiar resultados

**Panel Negro/Verde:**
- Estilo terminal/consola
- Logs de simulación

---

## 🧵 Conceptos de Concurrencia Demostrados

### 1. Múltiples Hilos
```
Cajera-1 (Thread) → Procesa Cliente-1
Cajera-2 (Thread) → Procesa Cliente-2  } Simultáneo
Cajera-3 (Thread) → Procesa Cliente-3
```

### 2. Cola Compartida (BlockingQueue)
```
[Cliente-1] [Cliente-2] [Cliente-3] [Cliente-4]
    ↓           ↓           ↓
Cajera-1    Cajera-2    Cajera-3
```

### 3. Variables Atómicas
- **AtomicLong**: Tiempo total acumulado (thread-safe)
- **DoubleAdder**: Monto total acumulado (thread-safe)
- Sin condiciones de carrera

### 4. Sincronización
- Cada cajera actualiza estadísticas de forma segura
- UI se actualiza usando `SwingUtilities.invokeLater()`
- No hay conflictos entre hilos

---

## 📊 Métricas de Simulación

### Tiempo Total
- **Definición**: Suma de todos los tiempos de procesamiento
- **Fórmula**: Σ(tiempo_cliente_i)
- **Nota**: NO es el tiempo real transcurrido (sería menor con concurrencia)

### Monto Total
- **Definición**: Suma de todas las ventas
- **Fórmula**: Σ(precio_producto_j) para todos los productos de todos los clientes

### Clientes Atendidos
- **Definición**: Número de clientes procesados completamente
- **Igual**: Número de clientes agregados antes de la simulación

---

## 🐛 Solución de Problemas

### Problema: No compila
**Solución:** Verifica que tienes JDK instalado
```cmd
java -version
javac -version
```

### Problema: Caracteres raros en pantalla
**Solución:** Usa `compilar.bat` que incluye `-encoding UTF-8`

### Problema: Interfaz no se abre
**Solución:** 
1. Verifica que se compiló correctamente
2. Asegúrate de ejecutar: `java -cp bin Main` (sin argumentos)
3. Verifica que tengas GUI disponible (no funciona en SSH)

### Problema: "No hay clientes"
**Solución:** Agrega al menos un cliente antes de iniciar simulación

### Problema: Simulación muy rápida
**Solución:** Aumenta el tiempo de procesamiento de productos (ej: 2000ms)

### Problema: Simulación muy lenta
**Solución:** Reduce el tiempo de procesamiento o usa menos productos

---

## 📝 Tips y Mejores Prácticas

### ✅ Recomendaciones

1. **Primera ejecución:**
   - Usa productos por defecto
   - Genera 3-5 clientes aleatorios
   - Configura 3 cajeras
   - Observa el comportamiento

2. **Pruebas de concurrencia:**
   - Prueba con 1 cajera → Anota tiempo
   - Prueba con 5 cajeras → Compara tiempo
   - Observa la paralelización

3. **Demostración visual:**
   - Usa tiempos de procesamiento altos (1000-2000ms)
   - Permite ver claramente el procesamiento paralelo

4. **Limpiar entre simulaciones:**
   - Usa "Limpiar Resultados" antes de cada simulación
   - Mantiene el panel legible

### ❌ Evitar

1. No agregues demasiados clientes (>20) sin necesidad
2. No uses tiempos muy bajos (<100ms) para demostraciones
3. No cierres la ventana durante una simulación en curso
4. No ejecutes múltiples instancias simultáneas

---

## 🎓 Objetivos Pedagógicos Cumplidos

### Concurrencia y Paralelismo
✅ Implementación de hilos (`Thread`)  
✅ Uso de `Runnable` interface  
✅ Gestión de cola compartida (`BlockingQueue`)  
✅ Sincronización con estructuras atómicas  
✅ Patrón Poison Pill para terminación  

### Programación Orientada a Objetos
✅ Clases bien estructuradas  
✅ Encapsulación de datos  
✅ Métodos cohesivos  
✅ Separación de responsabilidades  

### Interfaz Gráfica
✅ Java Swing profesional  
✅ Event-driven programming  
✅ Thread-safe UI updates  
✅ Diseño modular y escalable  

---

## 📞 Información del Proyecto

**Institución:** IUDigital de Antioquia  
**Departamento:** Tecnología en Desarrollo de Software  
**Tema:** Concurrencia y Programación con Hilos  
**Lenguaje:** Java (JDK 8+)  
**Framework GUI:** Java Swing  

---

**¡Ahora estás listo para usar el sistema!** 🎉

Para comenzar: Ejecuta `compilar.bat` y luego `ejecutar.bat`
