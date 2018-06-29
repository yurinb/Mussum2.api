package com.mussum.controllers;

import com.mussum.models.db.AdmLink;
import com.mussum.models.db.Feed;
import com.mussum.models.db.Horario;
import com.mussum.repository.FeedRepository;
import com.mussum.repository.HorarioRepository;
import com.mussum.repository.ProfessorRepository;
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
@RequestMapping("/api/horarios")
public class HorarioController {

    @Autowired
    private HorarioRepository horarioRep;

    @Autowired
    private FeedRepository feedRep;

    @Autowired
    private ProfessorRepository profRep;

    @Autowired
    private HttpServletRequest request;

    @GetMapping()
    @ResponseBody
    //@JsonIgnore
    public List<Horario> getHorarios() {
	return horarioRep.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Horario getHorario(@PathVariable Integer id) {
	return horarioRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Horario postHorario(@RequestBody @Valid Horario horario) {
	feedRep.save(new Feed(horario));
	return horarioRep.save(horario);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Horario putHorario(@RequestBody Horario horario, @PathVariable Integer id) {
	horario.setId(horarioRep.findById(id).get().getId());
	horarioRep.save(horario);
	return horario;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Horario deleteHorario(@PathVariable Integer id) {
	Horario horario = horarioRep.findById(id).get();
	horarioRep.delete(horario);
	return horario;
    }

}
