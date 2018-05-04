package com.mussum.models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "professor")
public class Professor extends Usuario {

    @NotBlank
    private String nome;

    private String email;

    private String sobre;

//    @OneToMany(mappedBy = "professor_id")
//    private List<Professor_Aviso> avisos;
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
