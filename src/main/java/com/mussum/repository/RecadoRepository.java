package com.mussum.repository;

import com.mussum.models.db.Professor;
import com.mussum.models.db.Recado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecadoRepository extends JpaRepository<Recado, Integer> {


    public List<Recado> findByProfessor(Professor professor);
    
}
