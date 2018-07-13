/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.controllers;

import com.mussum.models.db.Follower;
import com.mussum.models.db.Professor;
import com.mussum.repository.FollowerRepository;
import com.mussum.util.S;
import java.util.List;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private JavaMailSender mailSender;

    public void sendMailToFollowers(String dir, Professor professor, String fileName, FollowerRepository follRep, JavaMailSender mailSender) {
	this.mailSender = mailSender;
	String professorNome = professor.getNome();

	String msg1 = " adicionou novo arquivo na pasta que você seguis: ";
	String msg2 = "Arquivo: ";
	String msg3 = "Segue o link do repositório para acessar: ";

	String msg4 = "\n\n\n\nNão deseja receber mais notificações dessa pasta? chóris. ";

	String novaLinha = "\n";

	int numDiretorios = dir.split("/").length;
	String nomePasta = "";
	if (numDiretorios > 0) {
	    nomePasta = dir.split("/")[numDiretorios - 1];
	    S.out(nomePasta, this);
	}

	S.out("Followers: get by professor...", this);
	Follower foll = follRep.getOne(1);
	S.out(foll.getEmail(), this);
	List<Follower> followers = follRep.findAllByProfessor(professor);
	S.out("Followers finded", this);
	for (Follower follower : followers) {
	    S.out("Followers: follower X", this);
	    if (dir.startsWith(follower.getPastaDir())) {
		sendMail(follower.getEmail(), professorNome
			+ msg1 + nomePasta + novaLinha
			+ msg2 + fileName + novaLinha
			+ msg3 + "tuamae.com" + novaLinha
			+ msg4,
			"Novo Arquivis!!");
	    }

	}

    }

    public void sendMail(String email, String msg, String titulo) {
	SimpleMailMessage message = new SimpleMailMessage();

	message.setText(msg);
	message.setTo(email);
	message.setFrom("fatec.mussum@gmail.com");
	message.setSubject(titulo);
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		try {
		    mailSender.send(message);
		    S.out("Email enviado com sucesso.", this);
		} catch (MailException e) {
		    S.out(e.getLocalizedMessage(), this);
		}
	    }
	}).start();
    }

}
