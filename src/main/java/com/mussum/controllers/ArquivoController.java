package com.mussum.controllers;

import com.mussum.models.db.Feed;
import com.mussum.models.ftp.Arquivo;
import com.mussum.repository.ArquivoRepository;
import com.mussum.repository.FeedRepository;
import com.mussum.repository.ProfessorRepository;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/arquivos")
public class ArquivoController {

    @Autowired
    private ArquivoRepository arqRep;

    @Autowired
    private FeedRepository feedRep;

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ProfessorRepository profRe;

    @GetMapping()
    @ResponseBody
    //@JsonIgnore
    public List<Arquivo> getArquivos() {
        return arqRep.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Arquivo getArquivo(@PathVariable Integer id) {
        return arqRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Arquivo postArquivo(@RequestBody @Valid Arquivo arquivo) {
        Feed feed = new Feed(arquivo, profRe.findByUsername(context.getAttribute("requestUser").toString()).getNome());
        feedRep.save(feed);
        return arqRep.save(arquivo);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Arquivo putArquivo(@RequestBody Arquivo newArquivo, @PathVariable Integer id) {
        newArquivo.setId(arqRep.findById(id).get().getId());
        arqRep.save(newArquivo);
        return newArquivo;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Arquivo deleteArquivo(@PathVariable Integer id) {
        Arquivo arquivo = arqRep.findById(id).get();
        arqRep.delete(arquivo);
        return arquivo;
    }

}
