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
	Horario oldHorario = horarioRep.findById(id).get();
	horario.setId(oldHorario.getId());

	try {
	    List<Feed> oldFeeds = feedRep.findAllByTipoInAndTituloIn("horario", oldHorario.getTitulo());
	    Feed oldFeed = oldFeeds.get(0);
	    Feed update = new Feed(horario);
	    update.setId(oldFeed.getId());
	    feedRep.save(update);
	} catch (Exception e) {
	    //S.out("Feed nao econtrado" + e.getLocalizedMessage(), this);
	}

	horarioRep.save(horario);
	return horario;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Horario deleteHorario(@PathVariable Integer id) {
	Horario horario = horarioRep.findById(id).get();
	horarioRep.delete(horario);

	try {
	    List<Feed> oldFeeds = feedRep.findAllByTipoInAndTituloIn("horario", horario.getTitulo());
	    Feed oldFeed = oldFeeds.get(0);
	    feedRep.delete(oldFeed);
	} catch (Exception e) {
	    // S.out("Feed nao econtrado", this);
	}

	return horario;
    }

}
