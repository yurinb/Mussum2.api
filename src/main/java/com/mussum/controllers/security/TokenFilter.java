/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.controllers.security;

import io.jsonwebtoken.ExpiredJwtException;
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
import org.springframework.web.filter.GenericFilterBean;

/**
 *
 * @author yurin
 */
public class TokenFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
	System.out.println("Verificando TOKEN da requisição...");

	HttpServletRequest httpReq = (HttpServletRequest) sr;

	String header = httpReq.getHeader("Authorization");

	if (header == null || !header.startsWith("Bearer ")) {
	    throw new ServletException("Token inexistente ou inválido");
	}

	System.out.println("Header recebido: " + header);

	String token = header.substring(7);

	try {
	    Jwts.parser().setSigningKey("mussum").parseClaimsJws(token);
	} catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
	    if (e instanceof ExpiredJwtException) {
		throw new ServletException("Token expirado!");
	    }
	    throw new ServletException("Token inválido");
	}

	fc.doFilter(sr, sr1);
    }
}