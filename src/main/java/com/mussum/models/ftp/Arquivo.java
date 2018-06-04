package com.mussum.models.ftp;

import com.mussum.models.MussumEntity;
import java.time.LocalDate;
import javax.persistence.Entity;

@Entity
public class Arquivo extends MussumEntity {

    private String nome;

    private String dir;

    private LocalDate dataCriacao;

    public Arquivo() {
    }

    public Arquivo(String nome, String dir) {
        this.nome = nome;
        this.dir = dir;
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

}
