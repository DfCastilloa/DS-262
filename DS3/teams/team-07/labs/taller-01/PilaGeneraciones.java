/**
 * Pila (LIFO) que guarda copias de la malla.
 *
 * La última generación guardada es la primera que se recupera.
 * Está hecha con nodos enlazados: cada nodo apunta al que tiene debajo.
 */
public class PilaGeneraciones {

    private static class Nodo {
        boolean[][] malla;
        int numeroGeneracion;
        Nodo abajo;

        Nodo(boolean[][] malla, int numeroGeneracion, Nodo abajo) {
            this.malla = malla;
            this.numeroGeneracion = numeroGeneracion;
            this.abajo = abajo;
        }
    }

    private Nodo tope;
    private int tamano;

    /** Guarda una copia de la malla en el tope de la pila. O(1) sin contar la copia. */
    public void push(boolean[][] malla, int numeroGeneracion) {
        tope = new Nodo(copiar(malla), numeroGeneracion, tope);
        tamano++;
    }

    /** Saca y devuelve la malla del tope. O(1). */
    public boolean[][] pop() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }
        boolean[][] malla = tope.malla;
        tope = tope.abajo;
        tamano--;
        return malla;
    }

    /** Número de generación de la malla que está en el tope, sin sacarla. */
    public int generacionDelTope() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }
        return tope.numeroGeneracion;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    public int tamano() {
        return tamano;
    }

    /** Copia celda por celda para que la historia no cambie cuando cambie la malla actual. */
    private static boolean[][] copiar(boolean[][] original) {
        boolean[][] copia = new boolean[original.length][original[0].length];
        for (int f = 0; f < original.length; f++) {
            for (int c = 0; c < original[0].length; c++) {
                copia[f][c] = original[f][c];
            }
        }
        return copia;
    }
}
