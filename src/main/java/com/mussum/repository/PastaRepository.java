package com.mussum.repository;

import com.mussum.models.ftp.Pasta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PastaRepository extends JpaRepository<Pasta, Integer> {

//    @Query(value = "select p from Pasta p where p.dir=:pdir AND d from Pasta d where d.nome=:dnome")
//    public Pasta findByDirName(@Param("pdir") String dir, @Param("dnome") String nome);
    
    List<Pasta> findByDirInAndNomeIn(String dir, String nome);
    
    List<Pasta> findByDir(String dir);
    
}
