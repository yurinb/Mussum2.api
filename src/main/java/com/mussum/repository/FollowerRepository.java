package com.mussum.repository;

import com.mussum.models.db.Follower;
import com.mussum.models.db.Professor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepository extends JpaRepository<Follower, Integer> {
    
    public List<Follower> findByProfessor(Professor professor);

}
