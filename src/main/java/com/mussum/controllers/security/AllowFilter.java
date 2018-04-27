/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.controllers.security;

import io.jsonwebtoken.Jwts;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

/**
 *
 * @author yurin
 */
public class AllowFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
	System.out.println("Passando pelo filtro...");

	HttpServletResponse httpResp = (HttpServletResponse) sr1;

	String header = httpResp.getHeader("Authorization");

	if (header == null || !header.startsWith("Bearer ")) {
	    throw new ServletException("Token inexistente ou inválido");
	}
	System.out.println(header);

	String token = header.substring(7);

	try {
	    Jwts.parser().setSigningKey("mussum").parseClaimsJws(token);
	} catch (Exception e) {
	    throw new ServletException("Token inválido");
	}

	httpResp.setHeader("Access-Control-Allow-Origin", "*");
	fc.doFilter(sr, sr1);

    }

}
