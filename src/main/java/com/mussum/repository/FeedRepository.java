package com.mussum.repository;

import com.mussum.models.db.Feed;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Integer> {

    public List<Feed> findAllByDir(String dir);
    
}
