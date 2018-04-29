package com.mussum.controllers;

import com.mussum.models.Aviso;
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
    public List<Aviso> getAvisos() {
	return aviRep.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Aviso getAviso(@PathVariable Integer id) {
	return aviRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Aviso postAviso(@RequestBody @Valid Aviso aviso) {
	return aviRep.save(aviso);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Aviso putAviso(@RequestBody Aviso newAviso, @PathVariable Integer id) {
	Aviso old = aviRep.getOne(id);
	aviRep.save(old);
	return old;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Aviso deleteAviso(@PathVariable Integer id) {
	Aviso aviso = aviRep.findById(id).get();
	aviRep.delete(aviso);
	return aviso;
    }

}
