/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.controllers.security;

import com.mussum.repository.ProfessorRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

/**
 *
 * @author yurin
 */
public class TokenFilter extends GenericFilterBean {

    @Autowired
    private ProfessorRepository rep;

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        
        //IGNORANDO TOKEN ((TESTE))
        fc.doFilter(sr, sr1);

        System.out.println("Verificando TOKEN da requisição...");

        HttpServletRequest httpReq = (HttpServletRequest) sr;
        HttpServletResponse httpResp = (HttpServletResponse) sr1;

        String header = httpReq.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new ServletException("Token inexistente ou inválido");
        }

        System.out.println("Header recebido: " + header);

        String token = header.substring(7);

        try {
            Jwts.parser().setSigningKey("mussum").parseClaimsJws(token);
            String usuarioToken = Jwts.parser().setSigningKey("mussm")
                    .parse(token, new JwtHandlerAdapter<String>() {
                        @Override
                        public String onClaimsJws(Jws<Claims> jws) {
                            return jws.getBody().getSubject();
                        }
                    });

            System.out.println("Request by " + usuarioToken);
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            if (e instanceof ExpiredJwtException) {
                httpResp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                //throw new ServletException("Token expirado!");
            }
            throw new ServletException("Token inválido");
        }

        fc.doFilter(sr, sr1);
    }
}
