package com.mussum.controllers;

import com.mussum.models.db.Diretorio;
import com.mussum.models.db.Feed;
import com.mussum.repository.DiretorioRepository;
import com.mussum.repository.FeedRepository;
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
@RequestMapping("/api/diretorios")
public class DiretorioController {

    @Autowired
    private DiretorioRepository direRep;

    @Autowired
    private FeedRepository feedRep;

    @Autowired
    private HttpServletRequest context;

    @GetMapping()
    @ResponseBody
    public List<Diretorio> getDiretorios() {
	return direRep.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Diretorio getDiretorio(@PathVariable Integer id) {
	return direRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Diretorio postDiretorio(@RequestBody @Valid Diretorio dire) {
	Feed feed = new Feed(dire, context.getAttribute("requestUser").toString());
	feedRep.save(feed);
	return direRep.save(dire);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Diretorio putDiretorio(@RequestBody Diretorio newDire, @PathVariable Integer id) {
	Diretorio oldDiretorio = direRep.findById(id).get();
	newDire.setId(oldDiretorio.getId());

	try {
	    List<Feed> oldFeeds = feedRep.findAllByTipoInAndTituloIn("diretorio", oldDiretorio.getTitulo());
	    Feed oldFeed = oldFeeds.get(0);
	    Feed update = new Feed(newDire, oldFeed.getUsername());
	    update.setId(oldFeed.getId());
	    feedRep.save(update);
	} catch (Exception e) {
	    //S.out("Feed nao econtrado" + e.getLocalizedMessage(), this);
	}

	direRep.save(newDire);
	return newDire;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Diretorio deleteCurso(@PathVariable Integer id) {
	Diretorio dire = direRep.findById(id).get();
	direRep.delete(dire);

	try {
	    List<Feed> oldFeeds = feedRep.findAllByTipoInAndTituloIn("diretorio", dire.getTitulo());
	    Feed oldFeed = oldFeeds.get(0);
	    feedRep.delete(oldFeed);
	} catch (Exception e) {
	    // S.out("Feed nao econtrado", this);
	}

	return dire;
    }

}
