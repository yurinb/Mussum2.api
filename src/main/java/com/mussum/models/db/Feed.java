package com.mussum.models.db;

import com.mussum.models.MussumObject;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "feed")
public class Feed extends MussumObject {

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    private String acao; // exemplo: "upou" || "publicou" || "deletou" || "criou" || "alterou" ...

    @Column(name = "obj_link_name")
    private String objLinkName; // exemplo: "Atividade 03.docx" || "Aviso" || "Repositório" ...

    private String redirectUrl;

    @Column(name = "data_criacao")
    private String dataCriacao;

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getoQue() {
        return objLinkName;
    }

    public void setoQue(String oQue) {
        this.objLinkName = oQue;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getObjLinkName() {
        return objLinkName;
    }

    public void setObjLinkName(String objLinkName) {
        this.objLinkName = objLinkName;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

}
