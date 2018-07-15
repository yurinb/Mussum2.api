package com.mussum.controllers.security;

import com.mussum.util.S;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

public class TokenFilter extends GenericFilterBean {

    private HttpServletRequest hReq = null;
    private HttpServletResponse hRes = null;

    @Override
    public void doFilter(ServletRequest sReq, ServletResponse sRes, FilterChain fc) throws IOException, ServletException {

//        fc.doFilter(sr, sr1); //IGNORANDO TOKEN ((TESTE))
	hReq = (HttpServletRequest) sReq;
	hRes = (HttpServletResponse) sRes;
	S.out("", this);
	S.out("[[[ " + hReq.getMethod() + " ]]] " + " - " + hReq.getRequestURI(), this);
	S.out("", this);
	final String[] GET_BLOQUEADOS = {};
	final String[] POST_LIBERADOS = {"api/followers"};
	
	String username = getUser();
	S.out("REQUEST FILTER USER: " + username, this);

	if (username != null) {
	    hReq.setAttribute("requestUser", username);
	}

	if (hReq.getMethod().equals("GET")) {
	    if (!Arrays.asList(GET_BLOQUEADOS).contains(hReq.getRequestURI())) {
		S.out("GET: OK", this);
		hReq.setAttribute("requestUser", username);
		fc.doFilter(sReq, sRes);
		return;
	    }
	}

	if (hReq.getMethod().equals("POST")) {
	    if (Arrays.asList(POST_LIBERADOS).contains(hReq.getRequestURI())) {
		S.out("POST: OK", this);
		fc.doFilter(sReq, sRes);
		return;
	    }
	}

	if (hReq.getMethod().equals("OPTIONS")) {
	    S.out("OPTIONS: OK", this);
	    fc.doFilter(sReq, sRes);
	    return;
	}

	if (username == null) {
	    hRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "NÃO AUTORIZADO");
	    return;
	}

	//hReq.setAttribute("requestUser", username);
	fc.doFilter(sReq, sRes);

    }

    private String getUser() throws IOException {
	S.out("Verifing TOKEN...", this);

	String header = hReq.getHeader("Authorization");

	if (header == null || !header.startsWith("Bearer ")) {
	    //hRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Inexistent or invalid TOKEN!");
	    return null;
	}

	S.out("HEADER: " + header, this);

	String token = null;

	try {
	    token = header.split(" ")[1];
	} catch (Exception e) {
	    //hRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Inexistent or invalid TOKEN!");
	    return null;
	}

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

	    S.out("Request made by " + usuarioToken, this);
	    return usuarioToken;

	} catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
	    if (e instanceof ExpiredJwtException) {
		S.out("Expired TOKEN", this);
		//hRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired TOKEN!");
	    } else {
		S.out("Invalid TOKEN", this);
		//hRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid TOKEN!");
	    }
	    return null;
	}
    }

}
