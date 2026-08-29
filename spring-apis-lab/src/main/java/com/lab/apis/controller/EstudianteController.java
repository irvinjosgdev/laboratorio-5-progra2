package com.lab.apis.controller;

import com.lab.apis.model.Estudiante;
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
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final List<Estudiante> estudiantes = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong();

    public EstudianteController() {
        estudiantes.add(new Estudiante(contadorId.incrementAndGet(), "Maria", "Lopez", "Ingenieria en Sistemas", 21));
        estudiantes.add(new Estudiante(contadorId.incrementAndGet(), "Carlos", "Ramirez", "Administracion de Empresas", 23));
        estudiantes.add(new Estudiante(contadorId.incrementAndGet(), "Ana", "Garcia", "Diseno Grafico", 20));
        estudiantes.add(new Estudiante(contadorId.incrementAndGet(), "Luis", "Perez", "Ingenieria Civil", 22));
        estudiantes.add(new Estudiante(contadorId.incrementAndGet(), "Sofia", "Martinez", "Psicologia", 24));
    }

    @GetMapping
    public List<Estudiante> obtenerTodos() {
        return estudiantes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> obtenerPorId(@PathVariable Long id) {
        return buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Estudiante> crear(@RequestBody Estudiante estudiante) {
        estudiante.setId(contadorId.incrementAndGet());
        estudiantes.add(estudiante);
        return ResponseEntity.status(HttpStatus.CREATED).body(estudiante);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable Long id, @RequestBody Estudiante datos) {
        return buscarPorId(id)
                .map(estudiante -> {
                    estudiante.setNombre(datos.getNombre());
                    estudiante.setApellido(datos.getApellido());
                    estudiante.setCarrera(datos.getCarrera());
                    estudiante.setEdad(datos.getEdad());
                    return ResponseEntity.ok(estudiante);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Estudiante> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        return buscarPorId(id)
                .map(estudiante -> {
                    if (cambios.containsKey("nombre")) {
                        estudiante.setNombre((String) cambios.get("nombre"));
                    }
                    if (cambios.containsKey("apellido")) {
                        estudiante.setApellido((String) cambios.get("apellido"));
                    }
                    if (cambios.containsKey("carrera")) {
                        estudiante.setCarrera((String) cambios.get("carrera"));
                    }
                    if (cambios.containsKey("edad")) {
                        estudiante.setEdad(((Number) cambios.get("edad")).intValue());
                    }
                    return ResponseEntity.ok(estudiante);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = estudiantes.removeIf(estudiante -> estudiante.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private Optional<Estudiante> buscarPorId(Long id) {
        return estudiantes.stream()
                .filter(estudiante -> estudiante.getId().equals(id))
                .findFirst();
    }
}
