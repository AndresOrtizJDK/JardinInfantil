/*
 * Aplicacion de consola para registrar los ninos matriculados
 * en un jardin infantil (grados Prejardin y Jardin).
 *
 * Los estudiantes se guardan en memoria dentro de una lista
 * doblemente enlazada circular. 
 * 
 * estudiantes.txt para que la informacion no se pierda.
 *
 * Formato de cada linea del archivo:
 *   identificacion;nombreCompleto;sexo;edad;grado
 */

package com.mycompany.jardininfantil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class JardinInfantil {

    // Nombre del archivo principal donde se guardan los estudiantes
    static final String ARCHIVO = "estudiantes.txt";
    // Archivo temporal que se usa al borrar registros
    static final String ARCHIVO_TEMP = "estudiantes_temp.txt";

    // Lista doblemente enlazada circular con los estudiantes
    static ListaCircular lista = new ListaCircular();

    public static void main(String[] args) {
        // Al iniciar el programa se cargan los estudiantes que ya
        // estaban guardados en el archivo (si es que el archivo existe)
        cargarDesdeArchivo();

        Scanner sc = new Scanner(System.in);
        int opcion = 0;

        // Menu principal: se repite hasta que el usuario elija salir
        do {
            System.out.println("");
            System.out.println("=== JARDIN INFANTIL ===");
            System.out.println("1. Registrar estudiante");
            System.out.println("2. Buscar estudiante por identificacion");
            System.out.println("3. Borrar estudiante por identificacion");
            System.out.println("4. Generar informe");
            System.out.println("5. Salir");
            System.out.print("Elija una opcion: ");

            // Se lee la opcion como texto y luego se convierte a numero
            // para evitar errores si el usuario escribe algo no numerico
            String entrada = sc.nextLine();
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            switch (opcion) {
                case 1:
                    registrarEstudiante(sc);
                    break;
                case 2:
                    buscarEstudiante(sc);
                    break;
                case 3:
                    borrarEstudiante(sc);
                    break;
                case 4:
                    generarInforme();
                    break;
                case 5:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opcion no valida, intente de nuevo.");
            }

        } while (opcion != 5);

        sc.close();
    }

    /**
     * Pide los datos del estudiante por consola, valida que la edad
     * este entre 4 y 7 anos, que el grado sea valido y que la
     * identificacion no este registrada antes. Si todo es correcto
     * agrega el estudiante a la lista y reescribe el archivo.
     */
    static void registrarEstudiante(Scanner sc) {
        System.out.println("--- Registrar estudiante ---");

        System.out.print("Identificacion: ");
        String id = sc.nextLine().trim();

        // Se verifica que la identificacion no exista ya en la lista
        if (lista.buscar(id) != null) {
            System.out.println("Ya existe un estudiante con esa identificacion.");
            return;
        }

        System.out.print("Nombre completo: ");
        String nombre = sc.nextLine().trim();

        System.out.print("Sexo (F/M): ");
        String sexo = sc.nextLine().trim().toUpperCase();
        if (!sexo.equals("F") && !sexo.equals("M")) {
            System.out.println("Sexo no valido. Use F o M.");
            return;
        }

        System.out.print("Edad: ");
        int edad;
        try {
            edad = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("La edad debe ser un numero.");
            return;
        }
        if (edad < 4 || edad > 7) {
            System.out.println("La edad debe estar entre 4 y 7 anos.");
            return;
        }

        System.out.print("Grado (Prejardin/Jardin): ");
        String grado = sc.nextLine().trim();
        // Se acepta sin importar mayusculas o minusculas
        if (grado.equalsIgnoreCase("Prejardin")) {
            grado = "Prejardin";
        } else if (grado.equalsIgnoreCase("Jardin")) {
            grado = "Jardin";
        } else {
            System.out.println("Grado no valido. Use Prejardin o Jardin.");
            return;
        }

        // Se crea el estudiante y se agrega a la lista circular
        Estudiante nuevo = new Estudiante(id, nombre, sexo, edad, grado);
        lista.agregar(nuevo);

        // Se guarda la lista completa en el archivo
        guardarEnArchivo();
        System.out.println("Estudiante registrado correctamente.");
    }

    /**
     * Busca un estudiante en la lista usando la identificacion como
     * dato de busqueda y muestra sus datos por consola.
     */
    static void buscarEstudiante(Scanner sc) {
        System.out.println("--- Buscar estudiante ---");
        System.out.print("Identificacion a buscar: ");
        String id = sc.nextLine().trim();

        Nodo encontrado = lista.buscar(id);
        if (encontrado == null) {
            System.out.println("No se encontro un estudiante con esa identificacion.");
            return;
        }

        Estudiante e = encontrado.dato;
        System.out.println("Identificacion: " + e.identificacion);
        System.out.println("Nombre:         " + e.nombre);
        System.out.println("Sexo:           " + e.sexo);
        System.out.println("Edad:           " + e.edad);
        System.out.println("Grado:          " + e.grado);
    }

    /**
     * Borra un estudiante de la lista y reescribe el archivo usando
     * un archivo temporal como paso intermedio: primero se copia la
     * lista al archivo temporal, luego se borra el archivo original
     * y por ultimo se renombra el temporal con el nombre original.
     */
    static void borrarEstudiante(Scanner sc) {
        System.out.println("--- Borrar estudiante ---");
        System.out.print("Identificacion a borrar: ");
        String id = sc.nextLine().trim();

        boolean borrado = lista.eliminar(id);
        if (!borrado) {
            System.out.println("No se encontro un estudiante con esa identificacion.");
            return;
        }

        // Se escribe la lista actualizada en el archivo temporal
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_TEMP));
            if (!lista.estaVacia()) {
                Nodo actual = lista.cabeza;
                do {
                    pw.println(actual.dato.enLinea());
                    actual = actual.siguiente;
                } while (actual != lista.cabeza);
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo temporal: " + e.getMessage());
            return;
        }

        // Se borra el archivo original (si existe) y se renombra el temporal
        File original = new File(ARCHIVO);
        File temporal = new File(ARCHIVO_TEMP);

        if (original.exists()) {
            original.delete();
        }
        if (temporal.renameTo(new File(ARCHIVO))) {
            System.out.println("Estudiante borrado correctamente.");
        } else {
            System.out.println("No se pudo renombrar el archivo temporal.");
        }
    }

    /**
     * Recorre la lista una sola vez acumulando los datos necesarios
     * para mostrar el informe: cantidad de ninos y ninas por grado,
     * listados de nombres por grado y promedios de edad.
     */
    static void generarInforme() {
        System.out.println("--- Informe general ---");

        if (lista.estaVacia()) {
            System.out.println("Aun no hay estudiantes registrados.");
            return;
        }

        // Contadores por grado y por sexo
        int ninasPrejardin = 0;
        int ninosPrejardin = 0;
        int ninasJardin = 0;
        int ninosJardin = 0;

        // Para calcular promedios de edad por sexo en todo el jardin
        int sumaEdadNinas = 0;
        int totalNinas = 0;
        int sumaEdadNinos = 0;
        int totalNinos = 0;

        // Cadenas donde se van armando los listados de nombres
        String listaPrejardin = "";
        String listaJardin = "";

        // Recorrido de la lista circular: se empieza en la cabeza y
        // se avanza con siguiente hasta volver a la cabeza
        Nodo actual = lista.cabeza;
        do {
            Estudiante e = actual.dato;

            if (e.grado.equalsIgnoreCase("Prejardin")) {
                listaPrejardin += "  - " + e.nombre + "\n";
                if (e.sexo.equals("F")) {
                    ninasPrejardin++;
                } else {
                    ninosPrejardin++;
                }
            } else if (e.grado.equalsIgnoreCase("Jardin")) {
                listaJardin += "  - " + e.nombre + "\n";
                if (e.sexo.equals("F")) {
                    ninasJardin++;
                } else {
                    ninosJardin++;
                }
            }

            if (e.sexo.equals("F")) {
                sumaEdadNinas += e.edad;
                totalNinas++;
            } else {
                sumaEdadNinos += e.edad;
                totalNinos++;
            }

            actual = actual.siguiente;
        } while (actual != lista.cabeza);

        int totalPrejardin = ninasPrejardin + ninosPrejardin;
        int totalJardin = ninasJardin + ninosJardin;

        System.out.println("");
        System.out.println("Estudiantes en Prejardin: " + totalPrejardin);
        System.out.println("  Ninas: " + ninasPrejardin);
        System.out.println("  Ninos: " + ninosPrejardin);

        System.out.println("");
        System.out.println("Estudiantes en Jardin: " + totalJardin);
        System.out.println("  Ninas: " + ninasJardin);
        System.out.println("  Ninos: " + ninosJardin);

        System.out.println("");
        System.out.println("Listado de Prejardin:");
        if (listaPrejardin.equals("")) {
            System.out.println("  (sin estudiantes)");
        } else {
            System.out.print(listaPrejardin);
        }

        System.out.println("");
        System.out.println("Listado de Jardin:");
        if (listaJardin.equals("")) {
            System.out.println("  (sin estudiantes)");
        } else {
            System.out.print(listaJardin);
        }

        System.out.println("");
        System.out.println("Promedios de edad:");
        if (totalNinas > 0) {
            double promedioNinas = (double) sumaEdadNinas / totalNinas;
            System.out.println("  Ninas: " + promedioNinas + " anos");
        } else {
            System.out.println("  Ninas: no hay datos");
        }
        if (totalNinos > 0) {
            double promedioNinos = (double) sumaEdadNinos / totalNinos;
            System.out.println("  Ninos: " + promedioNinos + " anos");
        } else {
            System.out.println("  Ninos: no hay datos");
        }
    }

    /**
     * Lee el archivo de estudiantes (si existe) y carga cada linea
     * como un estudiante dentro de la lista circular. Asi cuando se
     * arranca el programa no se pierde la informacion guardada.
     */
    static void cargarDesdeArchivo() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length != 5) {
                    continue;
                }
                String id = datos[0];
                String nombre = datos[1];
                String sexo = datos[2];
                int edad;
                try {
                    edad = Integer.parseInt(datos[3]);
                } catch (NumberFormatException e) {
                    continue;
                }
                String grado = datos[4];

                Estudiante e = new Estudiante(id, nombre, sexo, edad, grado);
                lista.agregar(e);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    /**
     * Vuelca toda la lista en el archivo, sobrescribiendo lo que
     * hubiera antes. Se usa al registrar un nuevo estudiante.
     */
    static void guardarEnArchivo() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO));
            if (!lista.estaVacia()) {
                Nodo actual = lista.cabeza;
                do {
                    pw.println(actual.dato.enLinea());
                    actual = actual.siguiente;
                } while (actual != lista.cabeza);
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        }
    }
}
