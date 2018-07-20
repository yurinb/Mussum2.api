package com.mussum.models.db;

import com.mussum.models.MussumEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Follower extends MussumEntity {

    @NotBlank(message = "O campo email não pode ser nulo")
    private String email;

    @Column(columnDefinition = "TEXT")
    private String pastaDir;

    @ManyToOne()
    //@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    public Follower() {
    }

    public Follower(String email, String pastaDir, Professor professor) {
	this.email = email;
	this.pastaDir = pastaDir;
	this.professor = professor;
    }

    public Professor getProfessor() {
	return professor;
    }

    public void setProfessor(Professor professor) {
	this.professor = professor;

    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPastaDir() {
	return pastaDir;
    }

    public void setPastaDir(String pastaDir) {
	this.pastaDir = pastaDir;
    }

}
