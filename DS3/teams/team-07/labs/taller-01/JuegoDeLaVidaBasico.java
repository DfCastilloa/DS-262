/**
 * Primera versión del Juego de la Vida.
 *
 * Solo usa un arreglo 2D de 10 x 10 y las estructuras de control for, if y while.
 * No usa pilas, colas ni ninguna otra estructura de datos.
 */
public class JuegoDeLaVidaBasico {

    static final int TAMANO = 10;

    public static void main(String[] args) {
        // true = celda viva, false = celda muerta
        boolean[][] malla = new boolean[TAMANO][TAMANO];

        // Patrón inicial: un "glider" (se desplaza en diagonal)
        malla[0][1] = true;
        malla[1][2] = true;
        malla[2][0] = true;
        malla[2][1] = true;
        malla[2][2] = true;

        int generacion = 0;
        int totalGeneraciones = 4;

        while (generacion <= totalGeneraciones) {
            System.out.println("Generación " + generacion + ":");
            imprimir(malla);
            malla = siguienteGeneracion(malla);
            generacion++;
        }
    }

    /** Calcula la siguiente generación a partir de la actual. */
    static boolean[][] siguienteGeneracion(boolean[][] actual) {
        boolean[][] nueva = new boolean[TAMANO][TAMANO];

        for (int fila = 0; fila < TAMANO; fila++) {
            for (int col = 0; col < TAMANO; col++) {
                int vecinas = contarVecinasVivas(actual, fila, col);

                if (actual[fila][col]) {
                    // Una celda viva sobrevive con 2 o 3 vecinas vivas
                    nueva[fila][col] = (vecinas == 2 || vecinas == 3);
                } else {
                    // Una celda muerta nace con exactamente 3 vecinas vivas
                    nueva[fila][col] = (vecinas == 3);
                }
            }
        }
        return nueva;
    }

    /** Cuenta las vecinas vivas de una celda. Fuera de la malla todo se considera muerto. */
    static int contarVecinasVivas(boolean[][] malla, int fila, int col) {
        int vivas = 0;
        for (int df = -1; df <= 1; df++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (df == 0 && dc == 0) {
                    continue; // la celda no es vecina de sí misma
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

    static void imprimir(boolean[][] malla) {
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int col = 0; col < TAMANO; col++) {
                System.out.print(malla[fila][col] ? "# " : ". ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
