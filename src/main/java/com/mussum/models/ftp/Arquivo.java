package com.mussum.models.ftp;

import com.mussum.models.MussumEntity;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Entity;

@Entity
public class Arquivo extends MussumEntity {

    private String dir;

    private String nome;

    private boolean visivel = true;

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Arquivo comparing = (Arquivo) obj;
        return (comparing.getDir() + comparing.getNome()).equals(this.getDir() + this.getNome());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.dir);
        hash = 73 * hash + Objects.hashCode(this.nome);
        return hash;
    }

}
