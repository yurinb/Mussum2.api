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
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
	System.out.println("Liberando acesso da API *...");

	HttpServletRequest httpReq = (HttpServletRequest) sr;
	
	HttpServletResponse httpResp = (HttpServletResponse) sr1;

	httpResp.setHeader("Access-Control-Allow-Origin", "*");
	httpResp.setHeader("Access-Control-Allow-Headers", "*");
	httpResp.setHeader("Access-Control-Allow-Methods", "*");

	fc.doFilter(sr, sr1);

    }

}
