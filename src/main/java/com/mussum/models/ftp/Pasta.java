package com.mussum.models.ftp;

import java.util.ArrayList;
import java.util.List;

public class Pasta {

    private String dir;

    private final List<Pasta> folders = new ArrayList();

    private final List<Arquivo> files = new ArrayList();

    private boolean visivel = true;

    public Pasta(String nome) {
        this.dir = nome;
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

}
