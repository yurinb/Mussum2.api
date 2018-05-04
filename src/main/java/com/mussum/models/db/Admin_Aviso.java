package com.mussum.models.db;

import com.mussum.models.MussumObject;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admin_aviso")
public class Admin_Aviso extends MussumObject {

    private String titulo;

    private String detalhado;

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

}
