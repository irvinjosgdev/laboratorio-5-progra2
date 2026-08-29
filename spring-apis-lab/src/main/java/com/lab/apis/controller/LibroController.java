package com.lab.apis.controller;

import com.lab.apis.model.Libro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final List<Libro> libros = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong();

    public LibroController() {
        libros.add(new Libro(contadorId.incrementAndGet(), "Cien anos de soledad", "Gabriel Garcia Marquez", "Realismo magico", 180.0));
        libros.add(new Libro(contadorId.incrementAndGet(), "El principito", "Antoine de Saint-Exupery", "Fabula", 95.0));
        libros.add(new Libro(contadorId.incrementAndGet(), "1984", "George Orwell", "Distopia", 150.0));
        libros.add(new Libro(contadorId.incrementAndGet(), "El senor de los anillos", "J.R.R. Tolkien", "Fantasia", 220.0));
        libros.add(new Libro(contadorId.incrementAndGet(), "Clean Code", "Robert C. Martin", "Tecnico", 280.0));
    }

    @GetMapping
    public List<Libro> obtenerTodos() {
        return libros;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        return buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Libro> crear(@RequestBody Libro libro) {
        libro.setId(contadorId.incrementAndGet());
        libros.add(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(libro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @RequestBody Libro datos) {
        return buscarPorId(id)
                .map(libro -> {
                    libro.setTitulo(datos.getTitulo());
                    libro.setAutor(datos.getAutor());
                    libro.setGenero(datos.getGenero());
                    libro.setPrecio(datos.getPrecio());
                    return ResponseEntity.ok(libro);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Libro> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        return buscarPorId(id)
                .map(libro -> {
                    if (cambios.containsKey("titulo")) {
                        libro.setTitulo((String) cambios.get("titulo"));
                    }
                    if (cambios.containsKey("autor")) {
                        libro.setAutor((String) cambios.get("autor"));
                    }
                    if (cambios.containsKey("genero")) {
                        libro.setGenero((String) cambios.get("genero"));
                    }
                    if (cambios.containsKey("precio")) {
                        libro.setPrecio(((Number) cambios.get("precio")).doubleValue());
                    }
                    return ResponseEntity.ok(libro);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = libros.removeIf(libro -> libro.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private Optional<Libro> buscarPorId(Long id) {
        return libros.stream()
                .filter(libro -> libro.getId().equals(id))
                .findFirst();
    }
}
