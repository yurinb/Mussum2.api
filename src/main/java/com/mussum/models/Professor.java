package com.mussum.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Professor extends Usuario implements Serializable {

    @NotBlank
    private String nome;

    private String email;

    private String sobre;
    
    
    private List<Aviso> avisos;

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getSobre() {
	return sobre;
    }

    public void setSobre(String sobre) {
	this.sobre = sobre;
    }

}
