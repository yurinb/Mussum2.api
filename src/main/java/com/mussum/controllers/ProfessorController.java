package com.mussum.controllers;

import com.mussum.models.db.Professor;
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
@RequestMapping("/api/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository profRep;

    @GetMapping()
    @ResponseBody
    public List<Professor> getProfessores() {
	return profRep.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Professor getProfessor(@PathVariable Integer id) {
	return profRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Professor postProfessor(@RequestBody @Valid Professor prof) {
	return profRep.save(prof);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Professor putProfessor(@RequestBody Professor newProfessor, @PathVariable Integer id) {
	newProfessor.setId(profRep.findById(id).get().getId());
	profRep.save(newProfessor);
	return newProfessor;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Professor deleteProfessor(@PathVariable Integer id) {
	Professor prof = profRep.findById(id).get();
	profRep.delete(prof);
	return prof;
    }

}
