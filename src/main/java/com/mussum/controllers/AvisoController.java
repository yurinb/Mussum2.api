/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.controllers;

import com.mussum.models.Aviso;
import com.mussum.models.Professor;
import com.mussum.repository.AvisoRepository;
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

/**
 *
 * @author yurin
 */
@RestController
@RequestMapping("/api/avisos")
public class AvisoController {
    
   @Autowired
    private AvisoRepository aviRep;

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Aviso> listaAvisos() {
	return aviRep.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Aviso getProfessor(@PathVariable Integer id) {
	return aviRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Aviso novoProfessor(@RequestBody @Valid Aviso aviso) {
	return aviRep.save(aviso);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Aviso putProfessor(@RequestBody Aviso newAviso, @PathVariable Integer id) {
	Aviso old = aviRep.getOne(id);
	aviRep.save(old);
	return old;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Aviso remover(@PathVariable Integer id) {
	Aviso aviso = aviRep.findById(id).get();
	aviRep.delete(aviso);
	return aviso;
    }
    
}
