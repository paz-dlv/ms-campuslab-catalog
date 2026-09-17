package com.ms_catalogo.ms_catalogo.services;

import com.ms_catalogo.ms_catalogo.entities.Catalogo;
import com.ms_catalogo.ms_catalogo.entities.Tipo;

import java.util.List;
import java.util.Optional;

public interface CatalogoService {

    List<Catalogo> obtenerTodos();

    List<Catalogo> obtenerPorTipo(Tipo tipo);

    Optional<Catalogo> obtenerPorId(Long id);

    Catalogo crearRecurso(Catalogo catalogo);

    Optional<Catalogo> actualizarRecurso(Long id, Catalogo detalles);

    Catalogo reducirStock(Long id, Integer cantidad);

    boolean eliminarRecurso(Long id);
    
    List<Catalogo> obtenerRecursos(Tipo tipo);
}