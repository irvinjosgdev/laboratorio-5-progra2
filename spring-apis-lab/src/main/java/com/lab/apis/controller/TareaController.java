package com.lab.apis.controller;

import com.lab.apis.model.Tarea;
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
@RequestMapping("/api/tareas")
public class TareaController {

    private final List<Tarea> tareas = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong();

    public TareaController() {
        tareas.add(new Tarea(contadorId.incrementAndGet(), "Preparar entrega de laboratorio", "Terminar las 10 APIs REST del curso", "Alta", false));
        tareas.add(new Tarea(contadorId.incrementAndGet(), "Revisar documentacion de Spring", "Leer sobre controladores REST", "Media", true));
        tareas.add(new Tarea(contadorId.incrementAndGet(), "Configurar Postman", "Organizar la coleccion por carpetas", "Media", false));
        tareas.add(new Tarea(contadorId.incrementAndGet(), "Actualizar README", "Agregar instrucciones de ejecucion", "Baja", false));
        tareas.add(new Tarea(contadorId.incrementAndGet(), "Subir repositorio a GitHub", "Verificar historial de commits", "Alta", false));
    }

    @GetMapping
    public List<Tarea> obtenerTodos() {
        return tareas;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtenerPorId(@PathVariable Long id) {
        return buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tarea> crear(@RequestBody Tarea tarea) {
        tarea.setId(contadorId.incrementAndGet());
        tareas.add(tarea);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizar(@PathVariable Long id, @RequestBody Tarea datos) {
        return buscarPorId(id)
                .map(tarea -> {
                    tarea.setTitulo(datos.getTitulo());
                    tarea.setDescripcion(datos.getDescripcion());
                    tarea.setPrioridad(datos.getPrioridad());
                    tarea.setCompletada(datos.isCompletada());
                    return ResponseEntity.ok(tarea);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Tarea> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        return buscarPorId(id)
                .map(tarea -> {
                    if (cambios.containsKey("titulo")) {
                        tarea.setTitulo((String) cambios.get("titulo"));
                    }
                    if (cambios.containsKey("descripcion")) {
                        tarea.setDescripcion((String) cambios.get("descripcion"));
                    }
                    if (cambios.containsKey("prioridad")) {
                        tarea.setPrioridad((String) cambios.get("prioridad"));
                    }
                    if (cambios.containsKey("completada")) {
                        tarea.setCompletada((Boolean) cambios.get("completada"));
                    }
                    return ResponseEntity.ok(tarea);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = tareas.removeIf(tarea -> tarea.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private Optional<Tarea> buscarPorId(Long id) {
        return tareas.stream()
                .filter(tarea -> tarea.getId().equals(id))
                .findFirst();
    }
}
