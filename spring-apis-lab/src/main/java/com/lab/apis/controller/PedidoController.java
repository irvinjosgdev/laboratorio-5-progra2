package com.lab.apis.controller;

import com.lab.apis.model.Pedido;
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
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final List<Pedido> pedidos = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong();

    public PedidoController() {
        pedidos.add(new Pedido(contadorId.incrementAndGet(), "Jose Fuentes", "Laptop Lenovo ThinkPad", 1, 4500.0, "PENDIENTE"));
        pedidos.add(new Pedido(contadorId.incrementAndGet(), "Lucia Mendez", "Mouse Logitech M170", 2, 171.0, "ENVIADO"));
        pedidos.add(new Pedido(contadorId.incrementAndGet(), "Ricardo Aguilar", "Silla ergonomica", 1, 1200.0, "ENTREGADO"));
        pedidos.add(new Pedido(contadorId.incrementAndGet(), "Valeria Ortiz", "Escritorio de madera", 1, 950.0, "PENDIENTE"));
        pedidos.add(new Pedido(contadorId.incrementAndGet(), "Manuel Duarte", "Cuaderno profesional", 5, 125.0, "CANCELADO"));
    }

    @GetMapping
    public List<Pedido> obtenerTodos() {
        return pedidos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        return buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody Pedido pedido) {
        pedido.setId(contadorId.incrementAndGet());
        pedidos.add(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizar(@PathVariable Long id, @RequestBody Pedido datos) {
        return buscarPorId(id)
                .map(pedido -> {
                    pedido.setCliente(datos.getCliente());
                    pedido.setProducto(datos.getProducto());
                    pedido.setCantidad(datos.getCantidad());
                    pedido.setTotal(datos.getTotal());
                    pedido.setEstado(datos.getEstado());
                    return ResponseEntity.ok(pedido);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pedido> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        return buscarPorId(id)
                .map(pedido -> {
                    if (cambios.containsKey("cliente")) {
                        pedido.setCliente((String) cambios.get("cliente"));
                    }
                    if (cambios.containsKey("producto")) {
                        pedido.setProducto((String) cambios.get("producto"));
                    }
                    if (cambios.containsKey("cantidad")) {
                        pedido.setCantidad(((Number) cambios.get("cantidad")).intValue());
                    }
                    if (cambios.containsKey("total")) {
                        pedido.setTotal(((Number) cambios.get("total")).doubleValue());
                    }
                    if (cambios.containsKey("estado")) {
                        pedido.setEstado((String) cambios.get("estado"));
                    }
                    return ResponseEntity.ok(pedido);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = pedidos.removeIf(pedido -> pedido.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private Optional<Pedido> buscarPorId(Long id) {
        return pedidos.stream()
                .filter(pedido -> pedido.getId().equals(id))
                .findFirst();
    }
}
