package com.mussum.controllers.security;

import com.mussum.models.db.Professor;
import com.mussum.models.db.Usuario;
import com.mussum.repository.ProfessorRepository;
import com.mussum.util.S;
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

        S.out("_____________AUTH_____________", this);
        S.out("username: " + usuario.getUsername(), this);
        S.out("password: " + usuario.getPassword(), this);

        if (usuario.getUsername() == null || usuario.getPassword() == null) {

            S.out("ERROR: user or password are NULL", this);
            S.out("_______________________________", this);
            S.out("_______________*_______________", this);
            return new ResponseEntity<>("usuario e senha obrigatório!", HttpStatus.UNAUTHORIZED);

        }

        S.out("Authenticating...", this);

        Professor usuarioEncontrado = rep.findByUsername(usuario.getUsername());
        if (usuarioEncontrado == null) {

            S.out("ERROR: inexistent or invalid user", this);
            S.out("_______________________________", this);
            S.out("_______________*_______________", this);
            return new ResponseEntity<>("usuario inválido ou inexistente.", HttpStatus.UNAUTHORIZED);
        }

        if (!usuarioEncontrado.getPassword().equals(usuario.getPassword())) {

            S.out("ERROR: invalid password", this);
            S.out("_______________________________", this);
            S.out("_______________*_______________", this);
            return new ResponseEntity<>("senha inválida", HttpStatus.UNAUTHORIZED);
        }

        S.out("Authenticated.", this);
        S.out("_______________________________", this);
        S.out("_______________*_______________", this);

        String token = Jwts.builder()
                .setSubject(usuario.getUsername())
                .signWith(SignatureAlgorithm.HS512, "mussum")
                .setExpiration(new Date(System.currentTimeMillis() + (30 * 60 * 1000)))
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
