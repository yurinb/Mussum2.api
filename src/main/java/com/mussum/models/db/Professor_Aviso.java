package com.mussum.models.db;

import com.mussum.models.MussumObject;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "professor_aviso")
public class Professor_Aviso extends MussumObject {

    private String titulo;

    private String detalhado;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDetalhado() {
        return detalhado;
    }

    public void setDetalhado(String detalhado) {
        this.detalhado = detalhado;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

}
