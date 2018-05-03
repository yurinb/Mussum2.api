package com.mussum.controllers;

import com.mussum.models.db.Professor_Aviso;
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

@RestController
@RequestMapping("/api/avisos")
public class AvisoController {

    @Autowired
    private AvisoRepository aviRep;

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Professor_Aviso> getAvisos() {
	return aviRep.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Professor_Aviso getAviso(@PathVariable Integer id) {
	return aviRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Professor_Aviso postAviso(@RequestBody @Valid Professor_Aviso aviso) {
	return aviRep.save(aviso);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Professor_Aviso putAviso(@RequestBody Professor_Aviso newAviso, @PathVariable Integer id) {
	Professor_Aviso old = aviRep.getOne(id);
	aviRep.save(old);
	return old;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Professor_Aviso deleteAviso(@PathVariable Integer id) {
	Professor_Aviso aviso = aviRep.findById(id).get();
	aviRep.delete(aviso);
	return aviso;
    }

}
