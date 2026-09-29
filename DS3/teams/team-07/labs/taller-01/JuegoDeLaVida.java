import java.util.Scanner;

/**
 * Versión extendida del Juego de la Vida.
 *
 * - La malla sigue siendo un arreglo 2D de 10 x 10.
 * - Una pila guarda las generaciones anteriores para poder volver atrás (LIFO).
 * - Una cola define el orden en que se evalúan las celdas (FIFO).
 */
public class JuegoDeLaVida {

    static final int TAMANO = 10;

    private boolean[][] malla = new boolean[TAMANO][TAMANO];
    private int generacion = 0;
    private final PilaGeneraciones historial = new PilaGeneraciones();

    public JuegoDeLaVida() {
        // Glider: se desplaza una casilla en diagonal cada 4 generaciones
        malla[0][1] = true;
        malla[1][2] = true;
        malla[2][0] = true;
        malla[2][1] = true;
        malla[2][2] = true;

        // Blinker: oscila entre horizontal y vertical
        malla[7][6] = true;
        malla[7][7] = true;
        malla[7][8] = true;
    }

    /** Guarda la generación actual en la pila y avanza a la siguiente. */
    public void avanzar() {
        historial.push(malla, generacion);

        // 1. Se ponen en la cola todas las celdas, fila por fila.
        ColaCeldas pendientes = new ColaCeldas();
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int col = 0; col < TAMANO; col++) {
                pendientes.enqueue(fila, col);
            }
        }

        // 2. Se atienden en el mismo orden en que entraron (FIFO).
        //    Se lee de "malla" y se escribe en "nueva", así ninguna celda
        //    ve un valor ya actualizado de sus vecinas.
        boolean[][] nueva = new boolean[TAMANO][TAMANO];
        while (!pendientes.estaVacia()) {
            int[] celda = pendientes.dequeue();
            int fila = celda[0];
            int col = celda[1];
            nueva[fila][col] = estadoSiguiente(fila, col);
        }

        malla = nueva;
        generacion++;
    }

    /** Recupera la generación anterior sacándola de la pila. Devuelve false si no hay historial. */
    public boolean retroceder() {
        if (historial.estaVacia()) {
            return false;
        }
        generacion = historial.generacionDelTope();
        malla = historial.pop();
        return true;
    }

    /** Aplica las reglas del Juego de la Vida a una sola celda. */
    private boolean estadoSiguiente(int fila, int col) {
        int vecinas = contarVecinasVivas(fila, col);
        if (malla[fila][col]) {
            return vecinas == 2 || vecinas == 3; // sobrevive; si no, muere
        }
        return vecinas == 3; // nace
    }

    /** Cuenta las vecinas vivas. Fuera de la malla todo se considera muerto. */
    private int contarVecinasVivas(int fila, int col) {
        int vivas = 0;
        for (int df = -1; df <= 1; df++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (df == 0 && dc == 0) {
                    continue;
                }
                int f = fila + df;
                int c = col + dc;
                if (f >= 0 && f < TAMANO && c >= 0 && c < TAMANO && malla[f][c]) {
                    vivas++;
                }
            }
        }
        return vivas;
    }

    public void imprimir() {
        System.out.println("Generación " + generacion
                + "  (generaciones guardadas en la pila: " + historial.tamano() + ")");
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int col = 0; col < TAMANO; col++) {
                System.out.print(malla[fila][col] ? "# " : ". ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        JuegoDeLaVida juego = new JuegoDeLaVida();
        Scanner entrada = new Scanner(System.in);

        juego.imprimir();
        boolean seguir = true;
        while (seguir) {
            System.out.print("[s] siguiente  [a] anterior  [q] salir > ");
            if (!entrada.hasNextLine()) {
                break;
            }
            String opcion = entrada.nextLine().trim().toLowerCase();
            System.out.println();

            if (opcion.equals("s")) {
                juego.avanzar();
                juego.imprimir();
            } else if (opcion.equals("a")) {
                if (juego.retroceder()) {
                    juego.imprimir();
                } else {
                    System.out.println("No hay generaciones anteriores.\n");
                }
            } else if (opcion.equals("q")) {
                seguir = false;
            } else {
                System.out.println("Opción no válida.\n");
            }
        }
        System.out.println("Fin.");
    }
}
