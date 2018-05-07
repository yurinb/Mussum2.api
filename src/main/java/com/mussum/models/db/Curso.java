package com.mussum.models.db;

import com.mussum.models.SuperEntity;
import javax.persistence.Entity;

@Entity
public class Curso extends SuperEntity {

    private String titulo;

    public String getTitulo() {
	return titulo;
    }

    public void setTitulo(String titulo) {
	this.titulo = titulo;
    }

}
