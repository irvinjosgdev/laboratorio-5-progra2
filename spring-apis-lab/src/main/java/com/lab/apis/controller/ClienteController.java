package com.lab.apis.controller;

import com.lab.apis.model.Cliente;
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
@RequestMapping("/api/clientes")
public class ClienteController {

    private final List<Cliente> clientes = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong();

    public ClienteController() {
        clientes.add(new Cliente(contadorId.incrementAndGet(), "Jose", "Fuentes", "jose.fuentes@correo.com", "55123456"));
        clientes.add(new Cliente(contadorId.incrementAndGet(), "Lucia", "Mendez", "lucia.mendez@correo.com", "55234567"));
        clientes.add(new Cliente(contadorId.incrementAndGet(), "Ricardo", "Aguilar", "ricardo.aguilar@correo.com", "55345678"));
        clientes.add(new Cliente(contadorId.incrementAndGet(), "Valeria", "Ortiz", "valeria.ortiz@correo.com", "55456789"));
        clientes.add(new Cliente(contadorId.incrementAndGet(), "Manuel", "Duarte", "manuel.duarte@correo.com", "55567890"));
    }

    @GetMapping
    public List<Cliente> obtenerTodos() {
        return clientes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        return buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        cliente.setId(contadorId.incrementAndGet());
        clientes.add(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente datos) {
        return buscarPorId(id)
                .map(cliente -> {
                    cliente.setNombre(datos.getNombre());
                    cliente.setApellido(datos.getApellido());
                    cliente.setCorreo(datos.getCorreo());
                    cliente.setTelefono(datos.getTelefono());
                    return ResponseEntity.ok(cliente);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cliente> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        return buscarPorId(id)
                .map(cliente -> {
                    if (cambios.containsKey("nombre")) {
                        cliente.setNombre((String) cambios.get("nombre"));
                    }
                    if (cambios.containsKey("apellido")) {
                        cliente.setApellido((String) cambios.get("apellido"));
                    }
                    if (cambios.containsKey("correo")) {
                        cliente.setCorreo((String) cambios.get("correo"));
                    }
                    if (cambios.containsKey("telefono")) {
                        cliente.setTelefono((String) cambios.get("telefono"));
                    }
                    return ResponseEntity.ok(cliente);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = clientes.removeIf(cliente -> cliente.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private Optional<Cliente> buscarPorId(Long id) {
        return clientes.stream()
                .filter(cliente -> cliente.getId().equals(id))
                .findFirst();
    }
}
