package com.ms_catalogo.ms_catalogo.services;

import com.ms_catalogo.ms_catalogo.entities.Catalogo;
import com.ms_catalogo.ms_catalogo.entities.Estado;
import com.ms_catalogo.ms_catalogo.entities.Tipo;
import com.ms_catalogo.ms_catalogo.repositories.CatalogoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogoServiceImpl implements CatalogoService {

    private final CatalogoRepository catalogoRepository;

    public CatalogoServiceImpl(CatalogoRepository catalogoRepository) {
        this.catalogoRepository = catalogoRepository;
    }

    @Override
    public List<Catalogo> obtenerTodos() {
        return catalogoRepository.findAll();
    }

    @Override
    public List<Catalogo> obtenerPorTipo(Tipo tipo) {
        return catalogoRepository.findAll().stream()
                .filter(c -> c.getTipo() == tipo)
                .toList();
    }

    @Override
    public Optional<Catalogo> obtenerPorId(Long id) {
        return catalogoRepository.findById(id);
    }

    @Override
    public Catalogo crearRecurso(Catalogo catalogo) {
        if (catalogo.getStockDisponible() == null) {
            catalogo.setStockDisponible(catalogo.getStock());
        }
        if (catalogo.getEstado() == null) {
            catalogo.setEstado(Estado.DISPONIBLE);
        }
        return catalogoRepository.save(catalogo);
    }

    @Override
    public Optional<Catalogo> actualizarRecurso(Long id, Catalogo detalles) {
        return catalogoRepository.findById(id).map(existente -> {
            existente.setNombre(detalles.getNombre());
            existente.setTipo(detalles.getTipo());
            existente.setDescripcion(detalles.getDescripcion());
            existente.setEstado(detalles.getEstado());
            existente.setStock(detalles.getStock());
            existente.setStockDisponible(detalles.getStockDisponible());
            return catalogoRepository.save(existente);
        });
    }

    @Override
    @Transactional
    public Catalogo reducirStock(Long id, Integer cantidad) {
        Catalogo recurso = catalogoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado con id: " + id));

        if (recurso.getEstado() == Estado.NO_DISPONIBLE || recurso.getEstado() == Estado.MANTENCION) {
            throw new IllegalStateException("El recurso no se encuentra disponible");
        }

        if (recurso.getStockDisponible() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para realizar la reserva");
        }

        int nuevoStock = recurso.getStockDisponible() - cantidad;
        recurso.setStockDisponible(nuevoStock);

        if (nuevoStock == 0) {
            recurso.setEstado(Estado.NO_DISPONIBLE);
        }

        return catalogoRepository.save(recurso);
    }

    @Override
    public boolean eliminarRecurso(Long id) {
        if (catalogoRepository.existsById(id)) {
            catalogoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Catalogo> obtenerRecursos(Tipo tipo) {
        if (tipo != null) {
            return catalogoRepository.findByTipo(tipo);
        }
        return catalogoRepository.findAll();
    }
}