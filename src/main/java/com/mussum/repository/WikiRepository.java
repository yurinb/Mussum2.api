package com.mussum.repository;

import com.mussum.models.db.Wiki;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WikiRepository extends JpaRepository<Wiki, Integer> {

}
