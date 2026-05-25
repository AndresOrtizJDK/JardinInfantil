/*
 * Lista doblemente enlazada circular de estudiantes.
 *
 * En una lista circular el siguiente del ultimo nodo es la cabeza,
 * y el anterior de la cabeza es el ultimo nodo. Esto permite recorrer
 * la lista en cualquier direccion sin encontrar referencias nulas.
 *
 * La lista solo guarda un puntero a la cabeza. Cuando la lista esta
 * vacia, cabeza es null.
 */

package com.mycompany.jardininfantil;

public class ListaCircular {

    Nodo cabeza;
    int tamano;

    public ListaCircular() {
        this.cabeza = null;
        this.tamano = 0;
    }

    /**
     * Agrega un estudiante al final de la lista. Si la lista esta
     * vacia el nuevo nodo se apunta a si mismo en anterior y
     * siguiente para mantener la propiedad circular.
     */
    public void agregar(Estudiante e) {
        Nodo nuevo = new Nodo(e);
        if (cabeza == null) {
            cabeza = nuevo;
            nuevo.siguiente = nuevo;
            nuevo.anterior = nuevo;
        } else {
            Nodo ultimo = cabeza.anterior;
            // Se inserta el nuevo nodo entre el ultimo y la cabeza
            nuevo.anterior = ultimo;
            nuevo.siguiente = cabeza;
            ultimo.siguiente = nuevo;
            cabeza.anterior = nuevo;
        }
        tamano++;
    }

    /**
     * Busca un estudiante por su identificacion y devuelve el nodo
     * que lo contiene. Si no lo encuentra devuelve null.
     */
    public Nodo buscar(String identificacion) {
        if (cabeza == null) {
            return null;
        }
        Nodo actual = cabeza;
        // Se recorre la lista hasta dar la vuelta completa
        do {
            if (actual.dato.identificacion.equals(identificacion)) {
                return actual;
            }
            actual = actual.siguiente;
        } while (actual != cabeza);
        return null;
    }

    /**
     * Elimina el nodo que tenga la identificacion indicada. Devuelve
     * true si lo encontro y lo elimino, false en caso contrario.
     */
    public boolean eliminar(String identificacion) {
        Nodo nodo = buscar(identificacion);
        if (nodo == null) {
            return false;
        }

        // Si solo hay un nodo en la lista, la lista queda vacia
        if (nodo.siguiente == nodo) {
            cabeza = null;
        } else {
            // Se enlazan el anterior y el siguiente del nodo a eliminar
            nodo.anterior.siguiente = nodo.siguiente;
            nodo.siguiente.anterior = nodo.anterior;
            // Si el nodo que se elimina era la cabeza, se mueve la cabeza
            if (nodo == cabeza) {
                cabeza = nodo.siguiente;
            }
        }
        tamano--;
        return true;
    }

    /**
     * Indica si la lista esta vacia.
     */
    public boolean estaVacia() {
        return cabeza == null;
    }
}
