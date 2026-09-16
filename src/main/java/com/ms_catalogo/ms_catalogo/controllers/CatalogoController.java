package com.ms_catalogo.ms_catalogo.controllers;

import com.ms_catalogo.ms_catalogo.entities.Catalogo;
import com.ms_catalogo.ms_catalogo.entities.Estado;
import com.ms_catalogo.ms_catalogo.entities.Tipo;
import com.ms_catalogo.ms_catalogo.repositories.CatalogoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/resources")
public class CatalogoController {

    private final CatalogoRepository catalogoRepository;

    public CatalogoController(CatalogoRepository catalogoRepository) {
        this.catalogoRepository = catalogoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Catalogo>> listarRecursos(@RequestParam(required = false) Tipo tipo) {
        if (tipo != null) {
            List<Catalogo> filtrados = catalogoRepository.findAll().stream()
                    .filter(c -> c.getTipo() == tipo)
                    .toList();
            return ResponseEntity.ok(filtrados);
        }
        return ResponseEntity.ok(catalogoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Catalogo> obtenerPorId(@PathVariable Long id) {
        return catalogoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Catalogo> crearRecurso(@RequestBody Catalogo catalogo) {
        Catalogo nuevo = catalogoRepository.save(catalogo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Catalogo> actualizarRecurso(@PathVariable Long id, @RequestBody Catalogo detalles) {
        return catalogoRepository.findById(id).map(existente -> {
            existente.setNombre(detalles.getNombre());
            existente.setTipo(detalles.getTipo());
            existente.setDescripcion(detalles.getDescripcion());
            existente.setEstado(detalles.getEstado());
            existente.setStock(detalles.getStock());
            existente.setStockDisponible(detalles.getStockDisponible());
            Catalogo actualizado = catalogoRepository.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Catalogo> actualizarStock(@PathVariable Long id, @RequestParam Integer nuevoStockDisponible) {
        return catalogoRepository.findById(id).map(existente -> {
            existente.setStockDisponible(nuevoStockDisponible);
            if (nuevoStockDisponible <= 0) {
                existente.setEstado(Estado.NO_DISPONIBLE);
            }
            Catalogo actualizado = catalogoRepository.save(existente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRecurso(@PathVariable Long id) {
        if (catalogoRepository.existsById(id)) {
            catalogoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}