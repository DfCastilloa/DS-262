/**
 * Cola (FIFO) de celdas pendientes de evaluar.
 *
 * La primera celda que entra es la primera que se atiende.
 * Está hecha con nodos enlazados: se agrega por el final y se saca por el frente.
 */
public class ColaCeldas {

    private static class Nodo {
        int fila;
        int col;
        Nodo siguiente;

        Nodo(int fila, int col) {
            this.fila = fila;
            this.col = col;
        }
    }

    private Nodo frente;
    private Nodo fin;
    private int tamano;

    /** Agrega una celda al final de la cola. O(1). */
    public void enqueue(int fila, int col) {
        Nodo nuevo = new Nodo(fila, col);
        if (estaVacia()) {
            frente = nuevo;
        } else {
            fin.siguiente = nuevo;
        }
        fin = nuevo;
        tamano++;
    }

    /** Saca la celda del frente y devuelve {fila, col}. O(1). */
    public int[] dequeue() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía");
        }
        int[] celda = {frente.fila, frente.col};
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        tamano--;
        return celda;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int tamano() {
        return tamano;
    }
}
