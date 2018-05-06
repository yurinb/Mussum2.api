package com.mussum.models.db;

import com.mussum.models.MussumObject;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Recado extends MussumObject {

    private String titulo;

    private String descricao;

    private String data = LocalDateTime.now().toString();

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "professor_id")
    private Professor professor;

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

    public Professor getProfessor() {
	return professor;
    }

    public void setProfessor(Professor professor) {
	this.professor = professor;
    }

}
