# Laboratorio 5 - Programacion 2

APIs REST independientes construidas con Spring Boot y Maven, aplicando controladores REST y operaciones CRUD sobre listas en memoria.

## Objetivo

Desarrollar diez APIs REST independientes que expongan operaciones GET, POST, PUT, PATCH y DELETE, administrando cada una una lista en memoria de su propio modelo.

## APIs incluidas

| API | Endpoint base | Modelo |
|---|---|---|
| Productos | `/api/productos` | id, nombre, precio, categoria |
| Estudiantes | `/api/estudiantes` | id, nombre, apellido, carrera, edad |
| Libros | `/api/libros` | id, titulo, autor, genero, precio |
| Empleados | `/api/empleados` | id, nombre, puesto, salario, departamento |
| Peliculas | `/api/peliculas` | id, titulo, director, genero, anio |
| Cursos | `/api/cursos` | id, nombre, descripcion, creditos, modalidad |
| Vehiculos | `/api/vehiculos` | id, marca, modelo, anio, precio |
| Tareas | `/api/tareas` | id, titulo, descripcion, prioridad, completada |
| Clientes | `/api/clientes` | id, nombre, apellido, correo, telefono |
| Pedidos | `/api/pedidos` | id, cliente, producto, cantidad, total, estado |

Cada API mantiene su propia lista en memoria con al menos 5 registros iniciales y soporta:

```
GET     /api/<recurso>
GET     /api/<recurso>/{id}
POST    /api/<recurso>
PUT     /api/<recurso>/{id}
PATCH   /api/<recurso>/{id}
DELETE  /api/<recurso>/{id}
```

## Estructura del proyecto

```
LABORATORIO 5/
├── README.md
├── .gitignore
├── postman/
│   └── APIs-REST-LaboratorioV.postman_collection.json
└── spring-apis-lab/
    ├── pom.xml
    └── src/main/java/com/lab/apis/
        ├── ApisApplication.java
        ├── controller/
        │   ├── ProductoController.java
        │   ├── EstudianteController.java
        │   ├── LibroController.java
        │   ├── EmpleadoController.java
        │   ├── PeliculaController.java
        │   ├── CursoController.java
        │   ├── VehiculoController.java
        │   ├── TareaController.java
        │   ├── ClienteController.java
        │   └── PedidoController.java
        └── model/
            ├── Producto.java
            ├── Estudiante.java
            ├── Libro.java
            ├── Empleado.java
            ├── Pelicula.java
            ├── Curso.java
            ├── Vehiculo.java
            ├── Tarea.java
            ├── Cliente.java
            └── Pedido.java
```

## Como compilar y ejecutar

Requisitos: JDK 17 o superior y Maven.

```bash
cd spring-apis-lab
mvn spring-boot:run
```

La aplicacion queda disponible en `http://localhost:8080`. Por ejemplo:

```
GET http://localhost:8080/api/productos
```

## Coleccion de Postman

La coleccion `postman/APIs-REST-LaboratorioV.postman_collection.json` incluye una carpeta por cada API (01 Productos a 10 Pedidos), cada una con las seis solicitudes GET todos, GET por ID, POST, PUT, PATCH y DELETE. Se importa directamente en Postman y usa la variable de coleccion `base_url` apuntando a `http://localhost:8080`.
