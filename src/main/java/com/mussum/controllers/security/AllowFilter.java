package com.mussum.controllers.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

public class AllowFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain fc) throws IOException, ServletException {
        try {

            //System.out.println("Liberando acesso da API ...");
            HttpServletRequest httpReq = (HttpServletRequest) sreq;

            HttpServletResponse httpResp = (HttpServletResponse) sres;

            httpResp.setHeader("Access-Control-Allow-Origin", "*");
            httpResp.setHeader("Access-Control-Allow-Headers", httpReq.getHeader("Access-Control-Request-Headers"));
            httpResp.setHeader("Access-Control-Allow-Methods", "*");
        } catch (Exception e) {
        }

        fc.doFilter(sreq, sres);

    }

}
