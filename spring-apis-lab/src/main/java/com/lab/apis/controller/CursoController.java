package com.lab.apis.controller;

import com.lab.apis.model.Curso;
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
@RequestMapping("/api/cursos")
public class CursoController {

    private final List<Curso> cursos = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong();

    public CursoController() {
        cursos.add(new Curso(contadorId.incrementAndGet(), "Programacion 2", "Fundamentos de POO en Java", 4, "Presencial"));
        cursos.add(new Curso(contadorId.incrementAndGet(), "Bases de Datos", "Modelado y consultas SQL", 3, "Virtual"));
        cursos.add(new Curso(contadorId.incrementAndGet(), "Redes de Computadoras", "Protocolos y arquitectura de redes", 3, "Presencial"));
        cursos.add(new Curso(contadorId.incrementAndGet(), "Ingenieria de Software", "Metodologias agiles y ciclo de vida", 4, "Virtual"));
        cursos.add(new Curso(contadorId.incrementAndGet(), "Matematica Discreta", "Logica, conjuntos y grafos", 3, "Presencial"));
    }

    @GetMapping
    public List<Curso> obtenerTodos() {
        return cursos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerPorId(@PathVariable Long id) {
        return buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Curso> crear(@RequestBody Curso curso) {
        curso.setId(contadorId.incrementAndGet());
        cursos.add(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(curso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizar(@PathVariable Long id, @RequestBody Curso datos) {
        return buscarPorId(id)
                .map(curso -> {
                    curso.setNombre(datos.getNombre());
                    curso.setDescripcion(datos.getDescripcion());
                    curso.setCreditos(datos.getCreditos());
                    curso.setModalidad(datos.getModalidad());
                    return ResponseEntity.ok(curso);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Curso> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        return buscarPorId(id)
                .map(curso -> {
                    if (cambios.containsKey("nombre")) {
                        curso.setNombre((String) cambios.get("nombre"));
                    }
                    if (cambios.containsKey("descripcion")) {
                        curso.setDescripcion((String) cambios.get("descripcion"));
                    }
                    if (cambios.containsKey("creditos")) {
                        curso.setCreditos(((Number) cambios.get("creditos")).intValue());
                    }
                    if (cambios.containsKey("modalidad")) {
                        curso.setModalidad((String) cambios.get("modalidad"));
                    }
                    return ResponseEntity.ok(curso);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = cursos.removeIf(curso -> curso.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private Optional<Curso> buscarPorId(Long id) {
        return cursos.stream()
                .filter(curso -> curso.getId().equals(id))
                .findFirst();
    }
}
