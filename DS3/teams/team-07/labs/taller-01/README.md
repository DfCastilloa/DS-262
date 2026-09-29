# Taller 1 – Punto 2: Juego de la Vida con pila y cola

Implementación en Java del Juego de la Vida sobre una malla de 10 × 10. Hay dos versiones:

1. **Versión básica** (`JuegoDeLaVidaBasico.java`): solo usa un arreglo 2D con `for`, `if` y `while`. No usa ninguna otra estructura de datos.
2. **Versión extendida** (`JuegoDeLaVida.java`): usa la misma malla, pero agrega una **pila** para guardar el historial y una **cola** para definir el orden en que se evalúan las celdas.

## Archivos

| Archivo | Qué contiene |
| --- | --- |
| `JuegoDeLaVidaBasico.java` | Primera versión: malla 2D y reglas, sin estructuras adicionales. Muestra las generaciones 0 a 4. |
| `JuegoDeLaVida.java` | Versión extendida con menú: avanzar, retroceder y salir. |
| `PilaGeneraciones.java` | Pila (LIFO) con nodos enlazados. Guarda copias de la malla. |
| `ColaCeldas.java` | Cola (FIFO) con nodos enlazados. Guarda las coordenadas de las celdas pendientes. |

## Cómo compilar y ejecutar

Se necesita Java 17 o superior (se probó con Java 21). Desde esta carpeta:

```bash
javac -d out *.java

# Versión básica: imprime las generaciones 0 a 4
java -cp out JuegoDeLaVidaBasico

# Versión extendida: menú interactivo
java -cp out JuegoDeLaVida
```

En el menú: `s` avanza a la siguiente generación, `a` vuelve a la anterior y `q` sale. Las celdas vivas se muestran con `#` y las muertas con `.`.

**Prueba rápida:** si se escribe `s` cuatro veces, el *glider* baja una fila y se corre una columna a la derecha, y el *blinker* (abajo a la derecha) alterna entre horizontal y vertical. Si después se escribe `a` dos veces, se vuelve a ver la generación 3 y luego la 2, en ese orden. Si se escribe `a` cuando la pila está vacía, aparece "No hay generaciones anteriores."

---

## Explicación del código

### 1. Cómo se representa la malla de 10 × 10

La malla es un arreglo de dos dimensiones: `boolean[][] malla = new boolean[10][10]`. El primer índice es la fila y el segundo es la columna. Cada posición guarda `true` si la celda está viva y `false` si está muerta. Al crearse, todo el arreglo está en `false`, y después se encienden las celdas del patrón inicial.

Las celdas del borde tienen menos vecinas. Todo lo que queda por fuera de la malla se cuenta como muerto.

### 2. Cómo se decide el estado de una celda en la siguiente generación

Para cada celda se cuentan sus vecinas vivas, es decir, las 8 casillas que la rodean. Luego se aplican las reglas vistas en clase:

- Una celda **viva** con 2 o 3 vecinas vivas **sobrevive**.
- Una celda **viva** con menos de 2 vecinas muere por soledad, y con más de 3 muere por sobrepoblación.
- Una celda **muerta** con exactamente 3 vecinas vivas **nace**.
- En cualquier otro caso, la celda muerta sigue muerta.

Un detalle importante: la nueva generación se escribe en un **arreglo nuevo**. Si se cambiara la malla mientras se recorre, una celda podría contar a una vecina que ya pasó a la siguiente generación, y el resultado saldría mal. Por eso se lee siempre de la malla actual y se escribe en la nueva. Al terminar, la nueva pasa a ser la actual.

### 3. Cómo se usan los ciclos y las condiciones

- **`for`**: dos `for` anidados recorren todas las filas y columnas de la malla. Otros dos `for`, que van de −1 a 1, recorren las 8 vecinas de una celda.
- **`if`**:
  - Descartan la celda misma al contar vecinas.
  - Evitan salirse de la malla (el índice debe estar entre 0 y 9).
  - Aplican las reglas de vida o muerte.
  - Deciden qué hacer con cada opción del menú.
- **`while`**:
  - En la versión básica, repite el cálculo hasta llegar al número de generaciones pedido.
  - En la versión extendida, mantiene el menú activo hasta que el usuario sale.
  - También atiende las celdas de la cola hasta que se vacía.

### 4. Cómo se usa la pila para guardar y recuperar generaciones

Cada vez que se avanza, **antes** de calcular la siguiente generación se hace `push` de una **copia** de la malla actual. Se guarda una copia porque si se guardara la misma malla, cualquier cambio posterior también alteraría el historial.

Cuando el usuario pide volver atrás, se hace `pop`. Así se recupera la última generación guardada, que es justamente la inmediatamente anterior. Si se sigue retrocediendo, salen las generaciones en orden inverso: 4 → 3 → 2 → 1 → 0. Si la pila está vacía, se avisa que no hay más historial.

La pila está hecha con nodos enlazados. Cada nodo guarda una malla, su número de generación y una referencia al nodo de abajo. `push` y `pop` solo mueven la referencia del tope, así que cuestan O(1), sin contar la copia de la malla.

### 5. Cómo se usa la cola para ordenar el procesamiento de las celdas

Para calcular una generación, primero se ponen **todas las celdas en la cola** con `enqueue`, recorriendo la malla fila por fila, de izquierda a derecha. Después, mientras la cola no esté vacía, se saca una celda con `dequeue`, se calcula su nuevo estado y se escribe en la malla nueva.

Como la cola es FIFO, las celdas se atienden **en el mismo orden en que entraron**: (0,0), (0,1), …, (0,9), (1,0), …, (9,9). La cola separa dos pasos: decidir *qué* celdas hay que evaluar y *en qué orden* hacerlo.

La cola también usa nodos enlazados, con una referencia al frente y otra al final. `enqueue` y `dequeue` cuestan O(1).

### 6. Diferencia entre LIFO y FIFO en esta implementación

| | Pila (LIFO) | Cola (FIFO) |
| --- | --- | --- |
| Regla | El último en entrar es el primero en salir | El primero en entrar es el primero en salir |
| Qué guarda | Generaciones completas de la malla | Coordenadas de celdas |
| Para qué sirve | Volver atrás en el tiempo | Recorrer las celdas en un orden fijo |
| Orden en que salen | De la más reciente a la más antigua | En el mismo orden en que se agregaron |

Si el historial se guardara en una cola, "retroceder" devolvería la generación 0, no la anterior. Y si las celdas se guardaran en una pila, se evaluarían al revés, de (9,9) a (0,0). En este caso el resultado sería el mismo, porque se lee de una malla y se escribe en otra, pero se perdería el orden natural de recorrido.

### 7. Por qué cada estructura es apropiada para su función

- **Arreglo 2D para la malla:** el tablero es una cuadrícula de tamaño fijo, y el arreglo 2D la representa tal cual. Con `malla[fila][col]` se llega a cualquier celda en O(1), lo que facilita revisar sus vecinas.
- **Pila para el historial:** "deshacer" siempre necesita el estado más reciente, que es justo lo que entrega una pila con `pop`. Es el mismo principio del botón *deshacer* de un editor de texto.
- **Cola para las celdas:** garantiza un orden de atención justo y predecible, porque cada celda se evalúa en el turno en que llegó y ninguna se salta ni se repite. Además, deja lista la base para una mejora: meter en la cola solo las celdas vivas y sus vecinas, que son las únicas que pueden cambiar, en lugar de las 100 celdas.

## Complejidad

Sea *n* = 100, el número de celdas.

- Calcular una generación cuesta O(n): cada celda revisa 8 vecinas, que es una cantidad constante.
- Guardar una generación en la pila cuesta O(n) por la copia de la malla. El `push` en sí cuesta O(1).
- Retroceder cuesta O(1).
- La memoria del historial crece de a O(n) por cada generación que se avanza.

## Limitaciones

- El borde de la malla es fijo: lo que está por fuera se considera muerto, y la malla no "da la vuelta". Por eso el *glider* se detiene al llegar a la esquina.
- El patrón inicial está escrito en el código (un *glider* y un *blinker*).
- Si la consola no usa UTF-8, las tildes de los mensajes pueden verse mal. Se soluciona ejecutando con `java -Dstdout.encoding=UTF-8 -cp out JuegoDeLaVida`.
