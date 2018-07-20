package com.mussum.models.db;

import com.mussum.models.MussumEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Diretorio extends MussumEntity {

    @NotBlank(message = "O campo titulo não pode ser nulo")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "O campo url não pode ser nulo")
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
