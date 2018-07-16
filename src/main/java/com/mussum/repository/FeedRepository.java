package com.mussum.repository;

import com.mussum.models.db.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Integer> {

    public Feed findByDirInAndArquivoIn(String dir, String nome);

}
