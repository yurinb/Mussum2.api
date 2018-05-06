package com.mussum.models.db;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "professor")
public class Professor extends Usuario implements Serializable {

    @NotBlank
    private String nome;

    private String email;

    private String sobre;

    @OneToMany(cascade = {CascadeType.ALL})
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<ProfessorAviso> aviso;

    @OneToMany(cascade = {CascadeType.ALL})
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Feed> feeds;

    @OneToMany(cascade = {CascadeType.ALL})
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Professor_Link> links;

    public List<ProfessorAviso> getAvisos() {
        return aviso;
    }

    public void setAvisos(List<ProfessorAviso> avisos) {
        this.aviso = avisos;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public List<Professor_Link> getLinks() {
        return links;
    }

    public void setLinks(List<Professor_Link> links) {
        this.links = links;
    }

//    @OneToMany(mappedBy = "professor_id")
//    private List<Professor_Aviso> aviso;
//
//    @OneToMany(mappedBy = "professor_id")
//    private List<Professor_Link> links;
//
//    @OneToMany(mappedBy = "professor_id")
//    private List<Feed> feeds;
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

}
