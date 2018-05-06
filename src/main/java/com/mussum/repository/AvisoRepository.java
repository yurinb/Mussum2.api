package com.mussum.repository;

import com.mussum.models.db.Professor;
import com.mussum.models.db.ProfessorAviso;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvisoRepository extends JpaRepository<ProfessorAviso, Integer> {

    
    public List<ProfessorAviso> findByProfessor(Professor prof);
    
}
