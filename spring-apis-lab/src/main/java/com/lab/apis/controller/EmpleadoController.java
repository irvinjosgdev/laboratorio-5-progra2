package com.lab.apis.controller;

import com.lab.apis.model.Empleado;
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
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final List<Empleado> empleados = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong();

    public EmpleadoController() {
        empleados.add(new Empleado(contadorId.incrementAndGet(), "Gabriela Morales", "Gerente de Ventas", 12000.0, "Ventas"));
        empleados.add(new Empleado(contadorId.incrementAndGet(), "Diego Castillo", "Desarrollador de Software", 9500.0, "Tecnologia"));
        empleados.add(new Empleado(contadorId.incrementAndGet(), "Andrea Lima", "Analista Financiero", 8200.0, "Finanzas"));
        empleados.add(new Empleado(contadorId.incrementAndGet(), "Roberto Solis", "Coordinador de Logistica", 7500.0, "Operaciones"));
        empleados.add(new Empleado(contadorId.incrementAndGet(), "Paola Vargas", "Especialista en Recursos Humanos", 8000.0, "Recursos Humanos"));
    }

    @GetMapping
    public List<Empleado> obtenerTodos() {
        return empleados;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerPorId(@PathVariable Long id) {
        return buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Empleado> crear(@RequestBody Empleado empleado) {
        empleado.setId(contadorId.incrementAndGet());
        empleados.add(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(empleado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable Long id, @RequestBody Empleado datos) {
        return buscarPorId(id)
                .map(empleado -> {
                    empleado.setNombre(datos.getNombre());
                    empleado.setPuesto(datos.getPuesto());
                    empleado.setSalario(datos.getSalario());
                    empleado.setDepartamento(datos.getDepartamento());
                    return ResponseEntity.ok(empleado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Empleado> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        return buscarPorId(id)
                .map(empleado -> {
                    if (cambios.containsKey("nombre")) {
                        empleado.setNombre((String) cambios.get("nombre"));
                    }
                    if (cambios.containsKey("puesto")) {
                        empleado.setPuesto((String) cambios.get("puesto"));
                    }
                    if (cambios.containsKey("salario")) {
                        empleado.setSalario(((Number) cambios.get("salario")).doubleValue());
                    }
                    if (cambios.containsKey("departamento")) {
                        empleado.setDepartamento((String) cambios.get("departamento"));
                    }
                    return ResponseEntity.ok(empleado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = empleados.removeIf(empleado -> empleado.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private Optional<Empleado> buscarPorId(Long id) {
        return empleados.stream()
                .filter(empleado -> empleado.getId().equals(id))
                .findFirst();
    }
}
