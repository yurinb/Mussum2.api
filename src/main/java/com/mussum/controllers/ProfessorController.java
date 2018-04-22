package com.mussum.controllers;

import com.mussum.models.Professor;
import com.mussum.repository.ProfessorRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/professores")
public class ProfessorController {
    
    @Autowired
    private ProfessorRepository prep;
    
    @GetMapping(produces = "application/json")
    @ResponseBody
    public  List<Professor> listaProfessores() {
	return prep.findAll();
    }
    
    @PostMapping()
    public Professor novoProfessor(@RequestBody @Valid Professor prof) {
	return prep.save(prof);
    }
    
}
