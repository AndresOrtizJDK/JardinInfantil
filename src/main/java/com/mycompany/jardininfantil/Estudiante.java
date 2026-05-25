/*
 * Clase que representa la informacion de un estudiante del jardin
 * infantil. Solo guarda los datos, no hace operaciones.
 */

package com.mycompany.jardininfantil;

public class Estudiante {

    String identificacion;
    String nombre;
    String sexo;   // "F" o "M"
    int edad;      // debe estar entre 4 y 7
    String grado;  // "Prejardin" o "Jardin"

    // Constructor: recibe todos los datos y los asigna a los atributos
    public Estudiante(String identificacion, String nombre, String sexo,
                      int edad, String grado) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.sexo = sexo;
        this.edad = edad;
        this.grado = grado;
    }

    /**
     * Devuelve los datos del estudiante en una sola linea, separados
     * por punto y coma. Este formato se usa para guardar en el archivo.
     */
    public String enLinea() {
        return identificacion + ";" + nombre + ";" + sexo + ";" + edad + ";" + grado;
    }
}
