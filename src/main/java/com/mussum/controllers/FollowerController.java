package com.mussum.controllers;

import com.mussum.models.db.Follower;
import com.mussum.models.db.Professor;
import com.mussum.repository.FollowerRepository;
import com.mussum.repository.ProfessorRepository;
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
@RequestMapping("/api/followers")
public class FollowerController {

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private FollowerRepository follRep;

    @Autowired
    private ProfessorRepository profRep;

    @GetMapping()
    @ResponseBody
    //@JsonIgnore
    public ResponseEntity getFollowers() {
	String username = context.getHeader("username");
	if (username == null) {
	    return ResponseEntity.badRequest().body("Erro: Username não enviado.");

	} else {
	    return ResponseEntity.ok(follRep.findByProfessor(profRep.findByUsername(username)));
	}
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
	Follower newFollower = new Follower(payload.get("email"), payload.get("folderDir"), prof);
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
