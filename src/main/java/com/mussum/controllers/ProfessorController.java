package com.mussum.controllers;

import com.mussum.models.Professor;
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
@RequestMapping("admin/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository profRep;

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Professor> listaProfessores() {
	return profRep.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Professor getProfessor(@PathVariable Long id) {
	return profRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Professor novoProfessor(@RequestBody @Valid Professor prof) {
	return profRep.save(prof);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Professor putProfessor(@RequestBody Professor newProfwithOldId, @PathVariable Long id) {
	System.out.println("0");
	Professor old = profRep.getOne(id);
	System.out.println("1");
	old.setNome(newProfwithOldId.getNome());
	old.setEmail(newProfwithOldId.getEmail());
	old.setSobre(newProfwithOldId.getSobre());
	old.setUsername(newProfwithOldId.getUsername());
	old.setPassword(newProfwithOldId.getPassword());
	System.out.println("2");
	profRep.save(old);
	System.out.println("10");
	return old;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Professor remover(@PathVariable Long id) {
	Professor prof = profRep.findById(id).get();
	profRep.delete(prof);
	return prof;
    }

}
