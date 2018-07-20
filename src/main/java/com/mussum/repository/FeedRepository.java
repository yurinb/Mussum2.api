package com.mussum.repository;

import com.mussum.models.db.Feed;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Integer> {

    public Feed findByDirInAndArquivoIn(String dir, String nome);
    
    public Feed findByTipoInAndTituloInAndUsernameIn(String tipo, String titulo, String username);
    
    public List<Feed> findAllByTipoInAndTituloIn(String tipo, String titulo);

}
