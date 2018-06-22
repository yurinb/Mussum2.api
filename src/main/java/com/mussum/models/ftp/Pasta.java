package com.mussum.models.ftp;

import com.mussum.models.MussumEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Pasta extends MussumEntity {

    private String dir;

    private String nome;

    private boolean visivel = true;

    @OneToMany
    private final List<Pasta> folders = new ArrayList();

    @OneToMany
    private final List<Arquivo> files = new ArrayList();

    public Pasta() {
    }

    public Pasta(String dir, String nome) {
        this.dir = dir;
        this.nome = nome;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public List<Arquivo> getArquivos() {
        return files;
    }

    public List<Pasta> getPastas() {
        return folders;
    }

    public boolean isVisivel() {
        return visivel;
    }

    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Pasta comparing = (Pasta) obj;
        return (comparing.getDir() + comparing.getNome()).equals(this.getDir() + this.getNome());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.dir);
        hash = 67 * hash + Objects.hashCode(this.nome);
        return hash;
    }

}
