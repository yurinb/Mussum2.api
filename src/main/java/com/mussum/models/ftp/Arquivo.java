package com.mussum.models.ftp;

import com.mussum.models.MussumEntity;
import java.time.LocalDate;
import javax.persistence.Entity;

@Entity
public class Arquivo extends MussumEntity {

    private String dir;

    private String nome;

    private boolean visivel;

    private String comentario;

    private LocalDate dataCriacao;

    private String link;

    public Arquivo() {
    }

    public Arquivo(String dir, String nome) {
        this.nome = nome;
        this.dir = dir;
        this.comentario = "";
        this.dataCriacao = LocalDate.now();
        this.visivel = true;
        this.link = "";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public boolean isVisivel() {
        return visivel;
    }

    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
