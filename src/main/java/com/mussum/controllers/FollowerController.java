package com.mussum.controllers;

import com.mussum.models.db.Follower;
import com.mussum.models.db.Professor;
import com.mussum.models.ftp.Pasta;
import com.mussum.repository.FollowerRepository;
import com.mussum.repository.PastaRepository;
import com.mussum.repository.ProfessorRepository;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.HTML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/followers")
public class FollowerController {

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private FollowerRepository follRep;

    @Autowired
    private ProfessorRepository profRep;

    @Autowired
    private PastaRepository pastaRep;

    @GetMapping()
    @ResponseBody
    public ResponseEntity getFollowers() {
	String username = context.getHeader("username");
	if (username == null) {
	    return ResponseEntity.badRequest().body("Erro: Username não enviado.");

	} else {
	    return ResponseEntity.ok(follRep.findAllByProfessor(profRep.findByUsername(username)));
	}
    }

    @GetMapping("/removenotify/{followerId}/{folderId}")
    public ResponseEntity removeEmail(
	    @PathVariable int followerId,
	    @PathVariable int folderId) {
	Follower foundFollower = follRep.findById(followerId).get();
	Pasta foundFolder = pastaRep.findById(folderId).get();
	if (foundFollower.getPastaDir().equals(foundFolder.getDir() + "/" + foundFolder.getNome())) {
	    follRep.delete(foundFollower);
	    return new ResponseEntity("<h1>O email " + foundFollower.getEmail() + " não recebera mais notificações da pasta '" + pastaRep.getOne(folderId).getNome() + "'.</h1>", HttpStatus.OK);
	}
	return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Follower getFollower(@PathVariable Integer id) {
	return follRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Follower postFollower(@RequestBody Map<String, String> payload) {
	Professor prof = profRep.findByUsername(payload.get("username"));
	Follower newFollower = new Follower(payload.get("email"), payload.get("dir"), prof);
	return follRep.save(newFollower);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Follower putFollower(
	    @RequestBody Map<String, String> payload,
	    @PathVariable Integer id) {
	Follower follower = follRep.findById(id).get();
	follower.setEmail(payload.get("email"));
	follower.setPastaDir(payload.get("dir"));
	follRep.save(follower);
	return follower;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Follower deleteFollower(@PathVariable Integer id) {
	Follower follower = follRep.findById(id).get();
	follRep.delete(follower);
	return follower;
    }

}
