package com.mussum.models.ftp;

import com.mussum.models.SuperEntity;
import java.time.LocalDate;

public class Arquivo extends SuperEntity {

    private String nome;

    private double tamanho;

    private LocalDate dataCriacao;

    public Arquivo(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getTamanho() {
        return tamanho;
    }

    public void setTamanho(double tamanho) {
        this.tamanho = tamanho;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

}
