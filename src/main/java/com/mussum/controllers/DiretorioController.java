package com.mussum.controllers;

import com.mussum.models.db.Curso;
import com.mussum.models.db.Diretorio;
import com.mussum.repository.DiretorioRepository;
import java.util.List;
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
@RequestMapping("/api/diretorios")
public class DiretorioController {

    @Autowired
    private DiretorioRepository direRep;

    @GetMapping()
    @ResponseBody
    //@JsonIgnore
    public List<Diretorio> getDiretorios() {
	return direRep.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Diretorio getDiretorio(@PathVariable Integer id) {
	return direRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Diretorio postDiretorio(@RequestBody @Valid Diretorio dire) {
	return direRep.save(dire);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Diretorio putDiretorio(@RequestBody Diretorio newDire, @PathVariable Integer id) {
	newDire.setId(direRep.findById(id).get().getId());
	direRep.save(newDire);
	return newDire;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Diretorio deleteCurso(@PathVariable Integer id) {
	Diretorio dire = direRep.findById(id).get();
	direRep.delete(dire);
	return dire;
    }

}
