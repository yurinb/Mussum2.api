package com.mussum.models.db;

import com.mussum.models.MussumObject;
import java.time.LocalDateTime;
import javax.persistence.Entity;

@Entity
public class Aviso extends MussumObject {

    private String titulo;

    private String descricao;

    private String data = LocalDateTime.now().toString();

    private String url;

    public String getTitulo() {
	return titulo;
    }

    public void setTitulo(String titulo) {
	this.titulo = titulo;
    }

    public String getDescricao() {
	return descricao;
    }

    public void setDescricao(String descricao) {
	this.descricao = descricao;
    }

    public String getData() {
	return data;
    }

    public void setData(String data) {
	this.data = data;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

}
