package com.mussum.controllers;

import com.mussum.models.db.Professor;
import com.mussum.models.db.ProfessorAviso;
import com.mussum.repository.AvisoRepository;
import com.mussum.repository.ProfessorRepository;
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
@RequestMapping("/api/avisos")
public class AvisoController {

    @Autowired
    private AvisoRepository aviRep;

    @Autowired
    private ProfessorRepository profRep;

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<ProfessorAviso> getAvisos() {
        return aviRep.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ProfessorAviso getAviso(@PathVariable Integer id) {
        return aviRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public ProfessorAviso postAviso(@RequestBody @Valid ProfessorAviso aviso) {
        return aviRep.save(aviso);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ProfessorAviso putAviso(@RequestBody ProfessorAviso newAviso, @PathVariable Integer id) {
        ProfessorAviso old = aviRep.getOne(id);
        aviRep.save(old);
        return old;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ProfessorAviso deleteAviso(@PathVariable Integer id) {
        ProfessorAviso aviso = aviRep.findById(id).get();
        aviRep.delete(aviso);
        return aviso;
    }

    @GetMapping("/professor/{id}")
    @ResponseBody
    public List<ProfessorAviso> avisosByProfessorID(@PathVariable Integer id) {
        return aviRep.findByProfessor(profRep.findById(id).get());
    }

}
