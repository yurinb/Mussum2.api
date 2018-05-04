package com.mussum.repository;

import com.mussum.models.db.Professor;
import com.mussum.models.db.Professor_Aviso;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvisoRepository extends JpaRepository<Professor_Aviso, Integer> {

    
    public List<Professor_Aviso> findByProfessor(Professor prof);
    
}
