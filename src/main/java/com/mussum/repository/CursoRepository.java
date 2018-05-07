package com.mussum.repository;

import com.mussum.models.db.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Integer> {

}
