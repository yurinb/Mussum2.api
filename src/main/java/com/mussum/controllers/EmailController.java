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

    public void sendMailToFollowers(String dir, Professor professor, String fileName, FollowerRepository follRep, JavaMailSender mailSender, int pastaId) {
	this.mailSender = mailSender;
	String professorNome = professor.getNome() + " " + professor.getSobrenome();

	String msg1 = " adicionou novo arquivis na pasta que você seguis: ";
	String msg2 = "Arquivo: ";
	String msg3 = "Segue o link do repositóris para acessar: ";

	String msg4 = "\n\n\n\nNão deseja receber mais notificações dessa pasta? ";

	String novaLinha = "\n";

	int numDiretorios = dir.split("/").length;
	String nomePasta = "";
	if (numDiretorios > 0) {
	    nomePasta = dir.split("/")[numDiretorios - 1];
	}

	List<Follower> followers = follRep.findAllByProfessor(professor);

	if (followers.isEmpty()) {
	    return;
	}

	try {
	    String linkRepoDir = dir.substring(professor.getUsername().length() + 1);
	    linkRepoDir = linkRepoDir.replace(" ", "%20");
	    for (Follower follower : followers) {
		if (dir.startsWith(follower.getPastaDir())) {
		    sendMail(follower.getEmail(), professorNome
			    + msg1 + nomePasta + novaLinha
			    + msg2 + fileName + novaLinha
			    + msg3 + "http://mussum.ddns.net/professor/" + professor.getUsername() + "/diretorios/" + linkRepoDir
			    + msg4 + "http://mussum.ddns.net:8009/api/followers/removenotify/" + follower.getId() + "/" + pastaId,
			    "Novo Arquivis!!");
		}

	    }
	} catch (Exception e) {
	    S.out("ERRO: " + e.getLocalizedMessage(), this);
	}

    }

    public void sendMail(String email, String msg, String titulo) {
	SimpleMailMessage message = new SimpleMailMessage();

	message.setText(msg);
	message.setTo(email);
	message.setFrom("Mussum2.0");
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
