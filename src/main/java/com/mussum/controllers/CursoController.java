package com.mussum.controllers;

import com.mussum.models.db.Curso;
import com.mussum.repository.CursoRepository;
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
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRep;

    @GetMapping()
    @ResponseBody
    //@JsonIgnore
    public List<Curso> getCursos() {
	return cursoRep.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Curso getCurso(@PathVariable Integer id) {
	return cursoRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Curso postCurso(@RequestBody @Valid Curso curso) {
	return cursoRep.save(curso);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Curso putCurso(@RequestBody Curso newCurso, @PathVariable Integer id) {
	newCurso.setId(cursoRep.findById(id).get().getId());
	cursoRep.save(newCurso);
	return newCurso;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Curso deleteCurso(@PathVariable Integer id) {
	Curso curso = cursoRep.findById(id).get();
	cursoRep.delete(curso);
	return curso;
    }

}
