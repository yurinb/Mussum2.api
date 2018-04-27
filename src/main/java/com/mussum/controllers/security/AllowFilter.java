/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.controllers.security;

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
	System.out.println("passo pelo filtrao");

	HttpServletResponse httpResp = (HttpServletResponse) sr1;
	httpResp.setHeader("Access-Control-Allow-Origin", "*");
	
	System.out.println("colocou acess control");
	fc.doFilter(sr, sr1);

    }

}
