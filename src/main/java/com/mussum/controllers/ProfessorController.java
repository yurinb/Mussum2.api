package com.mussum.controllers;

import com.mussum.models.db.Professor;
import com.mussum.models.db.Social;
import com.mussum.repository.ProfessorRepository;
import com.mussum.util.S;
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
@RequestMapping("/api/professores")
public class ProfessorController {

    @Autowired
    private HttpServletRequest context;

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
	Social social = new Social();
	social.setProfessor(prof);
	return profRep.save(prof);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Professor putProfessor(
	    @RequestBody Professor newP,
	    @PathVariable Integer id) {
	String user = (String) context.getAttribute("requestUser");
	S.out("PUT REQ USER: " + user, this);
	Professor prof = profRep.findById(id).get();
	//newProfessor.setId(profRep.findById(id).get().getId());
	if (newP.getDescricao() != null) {
	    prof.setDescricao(newP.getDescricao());
	}
	if (newP.getEmail() != null) {
	    prof.setEmail(newP.getEmail());
	}
	if (newP.getFotolink() != null) {
	    prof.setFotolink(newP.getFotolink());
	}
	if (newP.getNome() != null) {
	    prof.setNome(newP.getNome());
	}
	if (newP.getPassword() != null) {
	    prof.setPassword(newP.getPassword());
	}
	if (newP.getRole() != null) {
	    prof.setRole(newP.getRole());
	}
	if (newP.getSobre() != null) {
	    prof.setSobre(newP.getSobre());
	}
	if (newP.getSobrenome() != null) {
	    prof.setSobrenome(newP.getSobrenome());
	}
	if (newP.getUsername() != null) {
	    prof.setUsername(newP.getUsername());
	}
	if (newP.getResumo() != null) {
	    prof.setResumo(newP.getResumo());
	}
	if (newP.getFormacao() != null) {
	    prof.setFormacao(newP.getFormacao());
	}
	profRep.save(prof);
	return prof;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Professor deleteProfessor(@PathVariable Integer id) {
	Professor prof = profRep.findById(id).get();
	profRep.delete(prof);
	return prof;
    }

}
