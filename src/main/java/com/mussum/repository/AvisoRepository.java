package com.mussum.repository;

import com.mussum.models.db.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvisoRepository extends JpaRepository<Aviso, Integer> {

}
