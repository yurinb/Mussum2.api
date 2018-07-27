package com.mussum.controllers.security;

import com.mussum.models.db.Professor;
import com.mussum.repository.ProfessorRepository;
import com.mussum.util.S;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
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
    public ResponseEntity<?> autenticar(@RequestBody Map<String, String> user) {

	S.out("_____________AUTH_____________", this);
	S.out("username: " + user.get("username"), this);
	S.out("password: " + user.get("password"), this);

	if (user.get("username") == null || user.get("password") == null) {

	    S.out("ERROR: user or password are NULL", this);
	    S.out("_______________*_______________", this);
	    return new ResponseEntity<>("usuario e senha obrigatório!", HttpStatus.UNAUTHORIZED);

	}

	S.out("Authenticating...", this);

	Professor usuarioEncontrado = rep.findByUsername(user.get("username"));
	if (usuarioEncontrado == null) {

	    S.out("ERROR: inexistent or invalid user", this);
	    S.out("_______________*_______________", this);
	    return new ResponseEntity<>("usuario inválido ou inexistente.", HttpStatus.UNAUTHORIZED);
	}

	if (!BCryptUtil.checkPassword(user.get("password"), usuarioEncontrado.getPassword())) {

	    S.out("ERROR: invalid password", this);
	    S.out("_______________*_______________", this);
	    return new ResponseEntity<>("senha inválida", HttpStatus.UNAUTHORIZED);
	}

	S.out("Authenticated.", this);
	S.out("_______________*_______________", this);

	String token = Jwts.builder()
		.setSubject(user.get("username"))
		.signWith(SignatureAlgorithm.HS512, "mussum")
		.setExpiration(new Date(System.currentTimeMillis() + (9999 * 60 * 1000)))
		.compact();

	String professorName = rep.findByUsername(usuarioEncontrado.getUsername()).getNome();

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
