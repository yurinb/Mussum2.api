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

    @Override
    public void doFilter(ServletRequest sReq, ServletResponse sRes, FilterChain fc) throws IOException, ServletException {

//        fc.doFilter(sr, sr1); //IGNORANDO TOKEN ((TESTE))
        HttpServletRequest hReq = (HttpServletRequest) sReq;
        HttpServletResponse hRes = (HttpServletResponse) sRes;

        S.out(hReq.getMethod() + " - " + hReq.getRequestURI(), this);
        final String[] GET_BLOQUEADOS = {};

        if (hReq.getMethod().equals("GET")) {
            if (!Arrays.asList(GET_BLOQUEADOS).contains(hReq.getRequestURI())) {
                S.out("GET: OK", this);
                fc.doFilter(sReq, sRes);
                return;
            }
        }

        if (hReq.getMethod().equals("OPTIONS")) {
            S.out("OPTIONS: OK", this);
            fc.doFilter(sReq, sRes);
            return;
        }

        S.out("Verifing TOKEN...", this);

        String header = hReq.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            hRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Inexistent or invalid TOKEN!");
        }

        S.out("HEADER: " + header, this);

        String token = null;

        try {
            token = header.split(" ")[1];
        } catch (Exception e) {
            hRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Inexistent or invalid TOKEN!");
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

        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            if (e instanceof ExpiredJwtException) {
                S.out("Expired TOKEN", this);
                hRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired TOKEN!");
            } else {
                hRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid TOKEN!");
            }
        }

        hReq.setAttribute("requestUser", usuarioToken);

        fc.doFilter(sReq, sRes);

    }
}
