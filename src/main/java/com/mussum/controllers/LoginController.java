/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.controllers;

import com.mussum.models.Usuario;
import com.mussum.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String autenticar(@RequestBody Usuario usuario) {

	System.out.println("AUTENTICANDO USUARIO:");
	System.out.println(usuario.getUsername());
	System.out.println(usuario.getPassword());
	
	
	
	return "logado pra caralho";
	
	//procurar por um professor no banco que bata com os dados recebidos (username/pass)
	//se o professor existe, gerar um token e devolver pro client
	//se nao, bom.. dai é obvio
    }

}
