package com.ms_catalogo.ms_catalogo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ms_catalogo.ms_catalogo.entities.Catalogo;
import com.ms_catalogo.ms_catalogo.entities.Tipo;

@Repository 
public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {
    List<Catalogo> findByTipo(Tipo tipo);
}
