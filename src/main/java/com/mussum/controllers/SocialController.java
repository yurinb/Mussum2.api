package com.mussum.controllers;

import com.mussum.models.db.Social;
import com.mussum.repository.ProfessorRepository;
import com.mussum.repository.SocialRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/social")
public class SocialController {

    @Autowired
    SocialRepository socRep;
    
    @Autowired
    ProfessorRepository profRep;

    @GetMapping()
    public List<Social> list() {
	return socRep.findAll();
    }

//    @GetMapping("/{id}")
//    public Social get(@PathVariable Integer id) {
//	return socRep.findById(id).get();
//    }
    
    @GetMapping("/{username}")
    public Social getByUsername(@PathVariable String username) {
	return socRep.findByProfessor(profRep.findByUsername(username));
    }
    

    @PutMapping("/{username}")
    public ResponseEntity put(@PathVariable String username, @RequestBody Social input) {
	input.setId(socRep.findByProfessor(profRep.findByUsername(username)).getId());
	input.setProfessor(profRep.findByUsername(username));
	socRep.save(input);
	return new ResponseEntity(input, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody Social input) {
	socRep.save(input);
	return new ResponseEntity(input, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
	socRep.deleteById(id);
	return new ResponseEntity(HttpStatus.OK);
    }

}
