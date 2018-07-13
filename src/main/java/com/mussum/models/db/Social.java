package com.mussum.models.db;

import com.mussum.models.MussumEntity;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Social extends MussumEntity {

    @ManyToOne()
    //@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    private String site;
    private String github;
    private String facebook;
    private String twitter;
    private String whatsapp;
    private String linkedin;
    private String google;
    private String youtube;

    public Professor getProfessor() {
	return professor;
    }

    public void setProfessor(Professor professor) {
	this.professor = professor;
    }

    public String getSite() {
	return site;
    }

    public void setSite(String site) {
	this.site = site;
    }

    public String getGithub() {
	return github;
    }

    public void setGithub(String github) {
	this.github = github;
    }

    public String getFacebook() {
	return facebook;
    }

    public void setFacebook(String facebook) {
	this.facebook = facebook;
    }

    public String getTwitter() {
	return twitter;
    }

    public void setTwitter(String twitter) {
	this.twitter = twitter;
    }

    public String getWhatsapp() {
	return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
	this.whatsapp = whatsapp;
    }

    public String getLinkedin() {
	return linkedin;
    }

    public void setLinkedin(String linkedin) {
	this.linkedin = linkedin;
    }

    public String getGoogle() {
	return google;
    }

    public void setGoogle(String google) {
	this.google = google;
    }

    public String getYoutube() {
	return youtube;
    }

    public void setYoutube(String youtube) {
	this.youtube = youtube;
    }
    
}
