package com.mussum.controllers.security;

import com.mussum.models.db.Professor;
import com.mussum.models.db.Usuario;
import com.mussum.repository.ProfessorRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private ProfessorRepository rep;

    @PostMapping()
    public ResponseEntity<?> autenticar(@RequestBody Usuario usuario) {

	System.out.println("Request TOKEN...");
	System.out.println("username: " + usuario.getUsername());
	System.out.println("password: " + usuario.getPassword());
	if (usuario.getUsername() == null || usuario.getPassword() == null) {
	    System.out.println("usuario e senha obrigatório.");
	    return new ResponseEntity<>("usuario e senha obrigatório!",HttpStatus.UNAUTHORIZED);

	}
	System.out.println("Autenticando usuario...");

	Professor usuarioEncontrado = rep.getByUsername(usuario.getUsername());
	if (usuarioEncontrado == null) {
	    System.out.println("usuario inválido ou inexistente.");
	    return new ResponseEntity<>("usuario inválido ou inexistente.",HttpStatus.UNAUTHORIZED);
	}

	if (!usuarioEncontrado.getPassword().equals(usuario.getPassword())) {
	    System.out.println("senha inválida.");
	    return new ResponseEntity<>("senha inválida",HttpStatus.UNAUTHORIZED);
	}

	System.out.println("Usuário autenticado.");

	String token = Jwts.builder()
		.setSubject(usuario.getUsername())
		.signWith(SignatureAlgorithm.HS512, "mussum")
		.setExpiration(new Date(System.currentTimeMillis() + (30 * 60 * 1000)))
		.compact();
	
	String professorName = rep.getByUsername(usuarioEncontrado.getUsername()).getNome();

	return new ResponseEntity<>(new TokenResponse(token, usuarioEncontrado.getRole(), professorName), HttpStatus.OK);
    }

    private class TokenResponse {

	public String token;

	public String role;
	
	public String nome;

	public TokenResponse(String token, String role, String nome) {
	    this.token = token;
	    this.role = role;
	    this.nome = nome;
	}
    }

}
