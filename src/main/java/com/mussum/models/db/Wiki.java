package com.mussum.models.db;

import com.mussum.models.SuperEntity;
import javax.persistence.Entity;

@Entity
public class Wiki extends SuperEntity {

    private String titulo;

    private String url;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
