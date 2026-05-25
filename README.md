# Jardín Infantil

Aplicación de consola en Java para registrar y gestionar los niños matriculados en un jardín infantil (grados **Prejardín** y **Jardín**). La información se almacena de forma persistente en un fichero plano.

## Enunciado

Se quiere almacenar en un fichero plano la información de los niños matriculados en un jardín infantil que maneja los grados de Prejardín y Jardín, teniendo en cuenta que la edad de los niños que ingresan a estos cursos debe estar entre los **4 y 7 años**. Entre los datos básicos que se quieren registrar de cada niño se encuentran: la **identificación**, **nombre completo** y **sexo**.

Además de permitir guardar la información de los estudiantes en el archivo, la directora del jardín infantil necesita que la aplicación le permita consultar la información de sus estudiantes y realice las siguientes operaciones sobre los datos:

- **Buscar** en el archivo la información de un niño, utilizando como dato de búsqueda la identificación, teniendo en cuenta que al ingresar la información de cada niño la identificación no se repite.
- **Borrar** el contenido de un registro específico del archivo, utilizando otro archivo temporal o auxiliar.

Se requiere generar un **informe detallado** que muestre:

- Cuántos estudiantes en total hay matriculados por cada grado (Jardín y Prejardín), determinando por separado el número de niñas y niños por grado.
- Un listado general con los nombres de todos los niños matriculados en Prejardín.
- Un listado general con los nombres de todos los niños matriculados en Jardín.
- Promedio de edad de los niños y de las niñas de todo el jardín infantil (ambos promedios por separado).

## Cómo lo solucionamos (resumen)

- **Modelo de datos:** la clase `Estudiante` guarda identificación, nombre, sexo, edad y grado. Su método `enLinea()` serializa el registro en el formato del archivo (`id;nombre;sexo;edad;grado`).
- **Estructura en memoria:** los estudiantes se mantienen en una **lista doblemente enlazada circular** (`ListaCircular` + `Nodo`), que permite recorrer los registros en cualquier dirección sin referencias nulas. Sobre ella se implementan `agregar`, `buscar` y `eliminar`.
- **Persistencia:** al iniciar, el programa carga el archivo `estudiantes.txt` a la lista (`cargarDesdeArchivo`). Cada registro nuevo vuelca la lista completa al archivo (`guardarEnArchivo`).
- **Búsqueda por identificación:** `buscar` recorre la lista circular comparando identificaciones; al registrar se valida que la identificación **no esté repetida**.
- **Borrado con archivo auxiliar:** al eliminar se escribe la lista actualizada en `estudiantes_temp.txt`, se borra el archivo original y se renombra el temporal a `estudiantes.txt`, cumpliendo el requisito de usar un archivo auxiliar.
- **Validaciones:** edad obligatoria entre 4 y 7 años, sexo `F`/`M` y grado `Prejardin`/`Jardin`.
- **Informe:** un único recorrido de la lista acumula contadores por grado y sexo, arma los listados de nombres por grado y calcula los promedios de edad de niñas y niños por separado.

## Estructura del proyecto

```
src/main/java/com/mycompany/jardininfantil/
├── Estudiante.java      # Datos de un estudiante y su serialización
├── Nodo.java            # Nodo de la lista doblemente enlazada
├── ListaCircular.java   # Lista circular: agregar, buscar, eliminar
└── JardinInfantil.java  # Programa principal: menú, archivo e informe
```

Archivos generados en tiempo de ejecución:

- `estudiantes.txt` — fichero plano con los registros.
- `estudiantes_temp.txt` — archivo auxiliar usado durante el borrado.

## Ejecución

Proyecto Maven (NetBeans). Desde la raíz del proyecto:

```bash
mvn clean package
java -cp target/classes com.mycompany.jardininfantil.JardinInfantil
```

O ejecutar directamente la clase `JardinInfantil` desde NetBeans.

## Menú

```
1. Registrar estudiante
2. Buscar estudiante por identificacion
3. Borrar estudiante por identificacion
4. Generar informe
5. Salir
```
