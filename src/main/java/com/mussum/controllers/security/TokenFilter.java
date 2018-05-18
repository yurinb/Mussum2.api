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

public class TokenFilter extends GenericFilterBean {

    @Autowired
    private ProfessorRepository rep;

    @Override
    public void doFilter(ServletRequest sReq, ServletResponse sRes, FilterChain fc) throws IOException, ServletException {

//        //IGNORANDO TOKEN ((TESTE))
//        fc.doFilter(sr, sr1);
	HttpServletRequest hReq = (HttpServletRequest) sReq;
	HttpServletResponse hRes = (HttpServletResponse) sRes;

	if (hReq.getMethod().equals("GET")) {
	    System.out.println(hReq.getRequestURI());
	    if (!hReq.getRequestURI().equals("/api/recados")) {
		System.out.println("GET request liberado.");
		fc.doFilter(sReq, sRes);
		return;
	    }

	}

	System.out.println("Verificando TOKEN da requisição...");

	String header = hReq.getHeader("Authorization");

	if (header == null || !header.startsWith("Bearer ")) {
	    hRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inexistente ou inválido!");
	}

	String token = header.substring(7);
	String usuarioToken = null;
	try {
	    Jwts.parser().setSigningKey("mussum").parseClaimsJws(token);
	    usuarioToken = Jwts.parser().setSigningKey("mussm")
		    .parse(token, new JwtHandlerAdapter<String>() {
			@Override
			public String onClaimsJws(Jws<Claims> jws) {
			    return jws.getBody().getSubject();
			}
		    });

	    System.out.println("Request by " + usuarioToken);

	    try {System.out.println("aaaaaaaaa " + rep.getByUsername(usuarioToken).getNome());
	    } catch (Exception e) {System.out.println(e);}
	    
	} catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
	    if (e instanceof ExpiredJwtException) {
		System.out.println("Token expirado!" + e);
		hRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expirado!");
	    } else {
		hRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido!");
	    }
	}

	hReq.setAttribute("requestUser", usuarioToken);

	fc.doFilter(sReq, sRes);

    }
}
