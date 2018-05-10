package com.mussum.models.db;

import com.mussum.models.SuperEntity;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Curso extends SuperEntity {

    @NotBlank(message = "O campo titulo não pode ser nulo")
    private String titulo;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
