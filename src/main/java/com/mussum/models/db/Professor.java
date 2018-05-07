package com.mussum.models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Professor extends Usuario implements Serializable {

    @NotBlank
    private String nome;

    private String email;

    private String sobre;

    private String descricao;

    private String fotolink = "http://franquia.globalmedclinica.com.br/wp-content/uploads/2016/01/investidores-img-02-01.png";

    @OneToMany(mappedBy = "professor", cascade = CascadeType.REMOVE)
    private Set<Recado> recados;

    public String getFotolink() {
	return fotolink;
    }

    public String getDescricao() {
	return descricao;
    }

    public void setDescricao(String descricao) {
	this.descricao = descricao;
    }

    public void setFotolink(String fotolink) {
	this.fotolink = fotolink;
    }

    @JsonIgnore
    public Set<Recado> getRecados() {
	return recados;
    }

    public void setRecados(Set<Recado> recados) {
	this.recados = recados;
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
