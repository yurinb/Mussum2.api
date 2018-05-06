package com.mussum.models.db;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Professor extends Usuario implements Serializable {

    @NotBlank
    private String nome;

    private String email;

    private String sobre;

    private String fotolink = "http://franquia.globalmedclinica.com.br/wp-content/uploads/2016/01/investidores-img-02-01.png";

    public String getFotoUrl() {
	return fotolink;
    }

    public void setFotoUrl(String fotoUrl) {
	this.fotolink = fotoUrl;
    }

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
