package com.ms_catalogo.ms_catalogo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ms_catalogo.ms_catalogo.entities.Catalogo;

@Repository 
public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {
    
}
