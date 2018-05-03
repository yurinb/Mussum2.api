package com.mussum.models.db;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Professor extends Usuario implements Serializable {

    @NotBlank
    private String nome;

    private String email;

    private String sobre;

    @OneToMany
    @JoinColumn(name = "professor_id")
    private List<Professor_Aviso> avisos;

    @OneToMany
    @JoinColumn(name = "professor_id")
    private List<Professor_Link> links;

    @OneToMany
    @JoinColumn(name = "professor_id")
    private List<Feed> feeds;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSobre() {
        return sobre;
    }

    public void setSobre(String sobre) {
        this.sobre = sobre;
    }

    public List<Professor_Aviso> getAvisos() {
        return avisos;
    }

    public List<Professor_Link> getLinks() {
        return links;
    }

    public void setLinks(List<Professor_Link> links) {
        this.links = links;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

}
