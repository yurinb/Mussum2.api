package com.mussum.repository;

import com.mussum.models.ftp.Arquivo;
import com.mussum.models.ftp.Pasta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArquivoRepository extends JpaRepository<Arquivo, Integer> {

//   @Query(value = "select p from Arquivo p where p.dir=:pdir AND d from Arquivo d where d.nome=:dnome")
//    public Arquivo findByDirName(@Param("pdir") String dir, @Param("dnome") String nome);
    List<Arquivo> findByDirInAndNomeIn(String dir, String nome);
    
}
