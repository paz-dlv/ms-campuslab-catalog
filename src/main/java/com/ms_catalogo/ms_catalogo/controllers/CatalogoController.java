package com.ms_catalogo.ms_catalogo.controllers;

import com.ms_catalogo.ms_catalogo.entities.Catalogo;
import com.ms_catalogo.ms_catalogo.entities.Tipo;
import com.ms_catalogo.ms_catalogo.services.CatalogoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/resources")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public ResponseEntity<List<Catalogo>> listarRecursos(@RequestParam(required = false) Tipo tipo) {
        if (tipo != null) {
            return ResponseEntity.ok(catalogoService.obtenerPorTipo(tipo));
        }
        return ResponseEntity.ok(catalogoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Catalogo> obtenerPorId(@PathVariable Long id) {
        return catalogoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Catalogo> crearRecurso(@RequestBody Catalogo catalogo) {
        Catalogo nuevo = catalogoService.crearRecurso(catalogo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Catalogo> actualizarRecurso(@PathVariable Long id, @RequestBody Catalogo detalles) {
        return catalogoService.actualizarRecurso(id, detalles)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/reducir-stock")
    public ResponseEntity<?> reducirStock(@PathVariable Long id, @RequestParam(defaultValue = "1") Integer cantidad) {
        try {
            Catalogo actualizado = catalogoService.reducirStock(id, cantidad);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRecurso(@PathVariable Long id) {
        if (catalogoService.eliminarRecurso(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}