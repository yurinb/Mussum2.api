package com.mussum.models.db;

import com.mussum.models.MussumEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Aviso extends MussumEntity {

    @NotBlank(message = "O campo titulo não pode ser nulo")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String data = LocalDateTime.now().toString();

    @Column(columnDefinition = "TEXT")
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
