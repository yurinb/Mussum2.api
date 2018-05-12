package com.mussum.models.ftp;

import java.time.LocalDate;

public class Arquivo {

    private String nome;

    private String dir;

    private long bytes;

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

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long tamanho) {
        this.bytes = tamanho;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

}
