package com.mussum.models;

import com.mussum.models.db.Usuario;
import java.util.List;

public class Pasta extends MussumObject {

    private Usuario proprietario;

    private String nome;

    private Pasta pastaPai;

    private List<Arquivo> arquivos;

    private List<Pasta> pastas;

    private boolean publica;

    public Usuario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Usuario proprietario) {
        this.proprietario = proprietario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Pasta getPastaPai() {
        return pastaPai;
    }

    public void setPastaPai(Pasta pastaPai) {
        this.pastaPai = pastaPai;
    }

    public List<Arquivo> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<Arquivo> arquivos) {
        this.arquivos = arquivos;
    }

    public List<Pasta> getPastas() {
        return pastas;
    }

    public void setPastas(List<Pasta> pastas) {
        this.pastas = pastas;
    }

    public boolean isPublica() {
        return publica;
    }

    public void setPublica(boolean publica) {
        this.publica = publica;
    }

}
