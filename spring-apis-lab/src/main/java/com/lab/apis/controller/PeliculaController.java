package com.lab.apis.controller;

import com.lab.apis.model.Pelicula;
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
@RequestMapping("/api/peliculas")
public class PeliculaController {

    private final List<Pelicula> peliculas = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong();

    public PeliculaController() {
        peliculas.add(new Pelicula(contadorId.incrementAndGet(), "El padrino", "Francis Ford Coppola", "Drama", 1972));
        peliculas.add(new Pelicula(contadorId.incrementAndGet(), "Origen", "Christopher Nolan", "Ciencia ficcion", 2010));
        peliculas.add(new Pelicula(contadorId.incrementAndGet(), "Coco", "Lee Unkrich", "Animacion", 2017));
        peliculas.add(new Pelicula(contadorId.incrementAndGet(), "Parasite", "Bong Joon-ho", "Thriller", 2019));
        peliculas.add(new Pelicula(contadorId.incrementAndGet(), "Interstellar", "Christopher Nolan", "Ciencia ficcion", 2014));
    }

    @GetMapping
    public List<Pelicula> obtenerTodos() {
        return peliculas;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pelicula> obtenerPorId(@PathVariable Long id) {
        return buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pelicula> crear(@RequestBody Pelicula pelicula) {
        pelicula.setId(contadorId.incrementAndGet());
        peliculas.add(pelicula);
        return ResponseEntity.status(HttpStatus.CREATED).body(pelicula);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pelicula> actualizar(@PathVariable Long id, @RequestBody Pelicula datos) {
        return buscarPorId(id)
                .map(pelicula -> {
                    pelicula.setTitulo(datos.getTitulo());
                    pelicula.setDirector(datos.getDirector());
                    pelicula.setGenero(datos.getGenero());
                    pelicula.setAnio(datos.getAnio());
                    return ResponseEntity.ok(pelicula);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pelicula> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        return buscarPorId(id)
                .map(pelicula -> {
                    if (cambios.containsKey("titulo")) {
                        pelicula.setTitulo((String) cambios.get("titulo"));
                    }
                    if (cambios.containsKey("director")) {
                        pelicula.setDirector((String) cambios.get("director"));
                    }
                    if (cambios.containsKey("genero")) {
                        pelicula.setGenero((String) cambios.get("genero"));
                    }
                    if (cambios.containsKey("anio")) {
                        pelicula.setAnio(((Number) cambios.get("anio")).intValue());
                    }
                    return ResponseEntity.ok(pelicula);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = peliculas.removeIf(pelicula -> pelicula.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private Optional<Pelicula> buscarPorId(Long id) {
        return peliculas.stream()
                .filter(pelicula -> pelicula.getId().equals(id))
                .findFirst();
    }
}
