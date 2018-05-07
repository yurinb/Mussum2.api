package com.mussum.controllers.security;

import com.mussum.models.db.Professor;
import com.mussum.models.db.Usuario;
import com.mussum.repository.ProfessorRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
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
    public TokenResponse autenticar(@RequestBody Usuario usuario) throws ServletException {

	System.out.println("Request TOKEN...");
	System.out.println("username: " + usuario.getUsername());
	System.out.println("password: " + usuario.getPassword());
	if (usuario.getUsername() == null || usuario.getPassword() == null) {
	    throw new ServletException("usuario e senha obrigatório.");
	}
	System.out.println("Autenticando usuario...");

	Professor usuarioEncontrado = rep.getByUsername(usuario.getUsername());
	if (usuarioEncontrado == null) {
	    throw new ServletException("usuario inválido ou inexistente.");
	}

	if (!usuarioEncontrado.getPassword().equals(usuario.getPassword())) {
	    throw new ServletException("senha inválida.");
	}

	System.out.println("Usuário autenticado.");

	String token = Jwts.builder()
		.setSubject(usuario.getUsername())
		.signWith(SignatureAlgorithm.HS512, "mussum")
		.setExpiration(new Date(System.currentTimeMillis() + (10 * 60 * 1000)))
		.compact();

	return new TokenResponse(token, usuarioEncontrado.getRole());
    }

    private class TokenResponse {

	public String token;
	
	public String role;

	public TokenResponse(String token, String role) {
	    this.token = token;
	    this.role = role;
	}
    }

}
