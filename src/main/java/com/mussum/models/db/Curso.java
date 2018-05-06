package com.mussum.models.db;

import com.mussum.models.MussumObject;
import javax.persistence.Entity;

@Entity
public class Curso extends MussumObject {

    private String titulo;

    public String getTitulo() {
	return titulo;
    }

    public void setTitulo(String titulo) {
	this.titulo = titulo;
    }

}
