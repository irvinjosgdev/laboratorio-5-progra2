package com.lab.apis.controller;

import com.lab.apis.model.Vehiculo;
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
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final List<Vehiculo> vehiculos = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong();

    public VehiculoController() {
        vehiculos.add(new Vehiculo(contadorId.incrementAndGet(), "Toyota", "Corolla", 2022, 145000.0));
        vehiculos.add(new Vehiculo(contadorId.incrementAndGet(), "Honda", "Civic", 2021, 152000.0));
        vehiculos.add(new Vehiculo(contadorId.incrementAndGet(), "Mazda", "CX-5", 2023, 210000.0));
        vehiculos.add(new Vehiculo(contadorId.incrementAndGet(), "Chevrolet", "Spark", 2020, 78000.0));
        vehiculos.add(new Vehiculo(contadorId.incrementAndGet(), "Kia", "Sportage", 2022, 195000.0));
    }

    @GetMapping
    public List<Vehiculo> obtenerTodos() {
        return vehiculos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerPorId(@PathVariable Long id) {
        return buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vehiculo> crear(@RequestBody Vehiculo vehiculo) {
        vehiculo.setId(contadorId.incrementAndGet());
        vehiculos.add(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable Long id, @RequestBody Vehiculo datos) {
        return buscarPorId(id)
                .map(vehiculo -> {
                    vehiculo.setMarca(datos.getMarca());
                    vehiculo.setModelo(datos.getModelo());
                    vehiculo.setAnio(datos.getAnio());
                    vehiculo.setPrecio(datos.getPrecio());
                    return ResponseEntity.ok(vehiculo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        return buscarPorId(id)
                .map(vehiculo -> {
                    if (cambios.containsKey("marca")) {
                        vehiculo.setMarca((String) cambios.get("marca"));
                    }
                    if (cambios.containsKey("modelo")) {
                        vehiculo.setModelo((String) cambios.get("modelo"));
                    }
                    if (cambios.containsKey("anio")) {
                        vehiculo.setAnio(((Number) cambios.get("anio")).intValue());
                    }
                    if (cambios.containsKey("precio")) {
                        vehiculo.setPrecio(((Number) cambios.get("precio")).doubleValue());
                    }
                    return ResponseEntity.ok(vehiculo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = vehiculos.removeIf(vehiculo -> vehiculo.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private Optional<Vehiculo> buscarPorId(Long id) {
        return vehiculos.stream()
                .filter(vehiculo -> vehiculo.getId().equals(id))
                .findFirst();
    }
}
