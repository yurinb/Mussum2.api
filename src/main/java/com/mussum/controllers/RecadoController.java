package com.mussum.controllers;

import com.mussum.models.db.Recado;
import com.mussum.repository.ProfessorRepository;
import com.mussum.repository.RecadoRepository;
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
@RequestMapping("/api/recados")
public class RecadoController {

    @Autowired
    private RecadoRepository recadoRep;
    @Autowired
    private ProfessorRepository profRep;

    @Autowired
    private HttpServletRequest context;

    @GetMapping()
    @ResponseBody
    //@JsonIgnore
    public List<Recado> getRecados() {
	if (context.getAttribute("requestUser").equals("null")) {
	    return recadoRep.findAll();
	}
	
	return recadoRep.findByProfessor(profRep.getByUsername((String) context.getAttribute("requestUser")));

    }

    @GetMapping("/{id}")
    @ResponseBody
    public Recado getRecado(@PathVariable Integer id) {
	return recadoRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Recado postRecado(@RequestBody @Valid Recado recado) {
	return recadoRep.save(recado);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Recado putRecado(@RequestBody Recado newRecado, @PathVariable Integer id) {
	newRecado.setId(recadoRep.findById(id).get().getId());
	recadoRep.save(newRecado);
	return newRecado;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Recado deleteRecado(@PathVariable Integer id) {
	Recado recado = recadoRep.findById(id).get();
	recadoRep.delete(recado);
	return recado;
    }

}
