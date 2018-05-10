package com.mussum.models.db;

import com.mussum.models.SuperEntity;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Wiki extends SuperEntity {

    @NotBlank(message = "O campo titulo não pode ser nulo")
    private String titulo;

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
