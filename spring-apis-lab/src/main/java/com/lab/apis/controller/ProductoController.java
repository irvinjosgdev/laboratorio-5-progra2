package com.lab.apis.controller;

import com.lab.apis.model.Producto;
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
@RequestMapping("/api/productos")
public class ProductoController {

    private final List<Producto> productos = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong();

    public ProductoController() {
        productos.add(new Producto(contadorId.incrementAndGet(), "Laptop Lenovo ThinkPad", 4500.00, "Electronica"));
        productos.add(new Producto(contadorId.incrementAndGet(), "Mouse Logitech M170", 85.50, "Electronica"));
        productos.add(new Producto(contadorId.incrementAndGet(), "Escritorio de madera", 950.00, "Mobiliario"));
        productos.add(new Producto(contadorId.incrementAndGet(), "Silla ergonomica", 1200.00, "Mobiliario"));
        productos.add(new Producto(contadorId.incrementAndGet(), "Cuaderno profesional", 25.00, "Papeleria"));
    }

    @GetMapping
    public List<Producto> obtenerTodos() {
        return productos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        producto.setId(contadorId.incrementAndGet());
        productos.add(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto datos) {
        return buscarPorId(id)
                .map(producto -> {
                    producto.setNombre(datos.getNombre());
                    producto.setPrecio(datos.getPrecio());
                    producto.setCategoria(datos.getCategoria());
                    return ResponseEntity.ok(producto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Producto> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        return buscarPorId(id)
                .map(producto -> {
                    if (cambios.containsKey("nombre")) {
                        producto.setNombre((String) cambios.get("nombre"));
                    }
                    if (cambios.containsKey("precio")) {
                        producto.setPrecio(((Number) cambios.get("precio")).doubleValue());
                    }
                    if (cambios.containsKey("categoria")) {
                        producto.setCategoria((String) cambios.get("categoria"));
                    }
                    return ResponseEntity.ok(producto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = productos.removeIf(producto -> producto.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private Optional<Producto> buscarPorId(Long id) {
        return productos.stream()
                .filter(producto -> producto.getId().equals(id))
                .findFirst();
    }
}
