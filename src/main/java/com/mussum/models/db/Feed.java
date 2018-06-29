package com.mussum.models.db;

import com.mussum.models.MussumEntity;
import com.mussum.models.ftp.Arquivo;
import com.mussum.util.S;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Entity;

@Entity
public class Feed extends MussumEntity {

    private String tipo = "";

    private String professor = "";

    private String dataCriacao = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

    private String titulo = "";

    private String comentario = "";

    private String arquivo = "";

    private String link = "";

    private String dir = "";

    private String username = "";

    public Feed() {
    }

    public Feed(Aviso aviso, Professor prof) {
        this.username = prof.getUsername();
        this.tipo = "aviso";
        this.professor = prof.getNome();
        this.titulo = aviso.getTitulo();
        this.comentario = aviso.getDescricao();
        S.out("new FEED: " + this.tipo, this);
    }
    public Feed(Wiki wiki, Professor prof) {
        this.username = prof.getUsername();
        this.tipo = "wiki";
        this.titulo = wiki.getTitulo();
        this.link = wiki.getUrl();
        S.out("new FEED: " + this.tipo, this);
    }

    public Feed(Recado recado) {
        this.username = recado.getProfessor().getUsername();
        this.tipo = "recado";
        this.professor = recado.getProfessor().getNome()+ " " + recado.getProfessor().getSobrenome();;
        this.titulo = recado.getTitulo();
        this.comentario = recado.getDescricao();
        S.out("new FEED: " + this.tipo, this);
    }

    public Feed(Arquivo arquivo, Professor prof) {
        this.username = prof.getUsername();
        if (arquivo.getLink().isEmpty()) {
            this.tipo = "upload";
            this.titulo = arquivo.getNome();
        } else {
            this.tipo = "link";
            this.titulo = arquivo.getNome().substring(0, arquivo.getNome().indexOf(".link"));
        }
        this.professor = prof.getNome()+ " " + prof.getSobrenome();
        this.arquivo = arquivo.getNome();
        this.link = arquivo.getLink();
        this.dir = arquivo.getDir();
        this.comentario = arquivo.getComentario();
        S.out("new FEED: " + this.tipo, this);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getData() {
        return dataCriacao;
    }

    public void setData(String data) {
        this.dataCriacao = data;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
