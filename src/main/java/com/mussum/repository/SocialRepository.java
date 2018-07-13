package com.mussum.repository;

import com.mussum.models.db.Professor;
import com.mussum.models.db.Social;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialRepository extends JpaRepository<Social, Integer> {

    public Social findByProfessor(Professor professor);
    
}
