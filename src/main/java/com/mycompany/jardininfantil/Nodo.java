/*
 * Nodo de la lista doblemente enlazada circular.
 *
 * Cada nodo guarda un estudiante y dos referencias: una al nodo
 * anterior y otra al nodo siguiente.
 */

package com.mycompany.jardininfantil;

public class Nodo {

    Estudiante dato;
    Nodo anterior;
    Nodo siguiente;

    public Nodo(Estudiante dato) {
        this.dato = dato;
        this.anterior = null;
        this.siguiente = null;
    }
}
