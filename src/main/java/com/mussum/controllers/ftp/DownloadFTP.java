package com.mussum.controllers.ftp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.io.InputStream;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class DownloadFTP {

    private final ControllerFTP ftp = new ControllerFTP();

    @GetMapping("/api/download")
    public ResponseEntity getFile(
            @RequestHeader("professor") String prof,
            @RequestHeader("dir") String dir,
            @RequestHeader("fileName") String fileName) {
        try {

            System.out.println("GETTING File: " + prof + dir + fileName);

            ftp.connect();
            InputStream file = ftp.getFile("/" + prof + dir + "/", fileName);
            ftp.getFtp().completePendingCommand();
            ftp.disconnect();
            if (file == null) {
                System.out.println("FILE NOT FOUND");
                return new ResponseEntity("Erro: arquivo do " + prof + " não encontrado.", HttpStatus.NOT_FOUND);
            }

            System.out.println("GETTING FILE" + fileName + ": SUCCESS!");
            return new ResponseEntity(StreamUtils.copyToByteArray(file), HttpStatus.OK);
        } catch (Exception ex) {
            ftp.disconnect();
            return new ResponseEntity("Erro: " + ex, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/api/photo")
    public ResponseEntity getProfessorPhoto(@RequestHeader("professor") String prof) {

        try {
            ftp.connect();

            System.out.println("GETTING User photo: " + prof);
            InputStream img = ftp.getFile("\\_res\\perfil_img\\", prof + ".png");
            ftp.getFtp().completePendingCommand();
            ftp.disconnect();

            if (img == null) {
                System.out.println("GETTING User photo: getFile returns NULL");
                return new ResponseEntity("Erro: img do professor " + prof + " não encontrada.", HttpStatus.NOT_FOUND);
            }

            System.out.println("GETTING User photo: SUCCESS!");
            return new ResponseEntity(StreamUtils.copyToByteArray(img), HttpStatus.OK);
        } catch (Exception ex) {
            ftp.disconnect();
            return new ResponseEntity("Erro: " + ex, HttpStatus.BAD_REQUEST);
        }

    }

}
