package com.mussum.controllers;

import com.mussum.models.db.Feed;
import com.mussum.models.db.Professor;
import com.mussum.models.db.Recado;
import com.mussum.repository.FeedRepository;
import com.mussum.repository.ProfessorRepository;
import com.mussum.repository.RecadoRepository;
import com.mussum.controllers.ftp.utils.S;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private HttpServletRequest context;

    @Autowired
    private RecadoRepository recadoRep;

    @Autowired
    private ProfessorRepository profRep;

    @Autowired
    private FeedRepository feedRep;

    @GetMapping()
    @ResponseBody
    public ResponseEntity getRecados() {
	String username = context.getHeader("username");
	S.out("GET Recados from " + username, this);
	if (username == null) {
	    return ResponseEntity.badRequest().body("Erro: Username não enviado.");

	} else {
	    return ResponseEntity.ok(recadoRep.findByProfessor(profRep.findByUsername(username)));
	}
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Recado getRecado(@PathVariable Integer id) {
	return recadoRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Recado postRecado(@RequestBody Recadof recado) {
	String requestUser = (String) context.getAttribute("requestUser");
	Professor prof = profRep.findByUsername(requestUser);
	Recado newRecado = new Recado(recado.titulo, recado.descricao, prof);
	Feed feed = new Feed(newRecado);
	feedRep.save(feed);
	return recadoRep.save(newRecado);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Recado putRecado(
	    @RequestBody Map<String, String> payload,
	    @PathVariable Integer id) {
	Recado recado = recadoRep.findById(id).get();
	String oldTitulo = recado.getTitulo();
	recado.setTitulo(payload.get("titulo"));
	recado.setDescricao(payload.get("descricao"));
	recadoRep.save(recado);
	try {
	    List<Feed> oldFeeds = feedRep.findAllByTipoInAndTituloIn("recado", oldTitulo);
	    Feed oldFeed = oldFeeds.get(0);
	    Feed update = new Feed(recado);
	    update.setId(oldFeed.getId());
	    feedRep.save(update);
	} catch (Exception e) {
	    // S.out("Feed nao econtrado", this);
	}

	return recado;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Recado deleteRecado(@PathVariable Integer id) {
	Recado recado = recadoRep.findById(id).get();
	recadoRep.delete(recado);

	try {
	    List<Feed> oldFeeds = feedRep.findAllByTipoInAndTituloIn("recado", recado.getTitulo());
	    Feed feed = oldFeeds.get(0);
	    feedRep.delete(feed);
	} catch (Exception e) {
	    // S.out("Feed nao econtrado", this);
	}

	return recado;
    }

}

class Recadof {

    public String titulo;
    public String descricao;

    public Recadof() {
    }

    public Recadof(String titulo, String descricao) {
	this.titulo = titulo;
	this.descricao = descricao;
    }

    public String getTitulo() {
	return titulo;
    }

    public void setTitulo(String titulo) {
	this.titulo = titulo;
    }

    public String getDescricao() {
	return descricao;
    }

    public void setDescricao(String descricao) {
	this.descricao = descricao;
    }

}
