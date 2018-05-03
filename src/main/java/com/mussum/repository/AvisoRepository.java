package com.mussum.repository;

import com.mussum.models.db.Professor_Aviso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvisoRepository extends JpaRepository<Professor_Aviso, Integer> {

    
}
