package com.mussum.controllers.ftp;

import java.io.ByteArrayOutputStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.io.InputStream;
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
            ftp.connect();

            System.out.println("GETTING File from: " + prof);
            System.out.println("GETTING File: " + dir);
            InputStream file = ftp.getFile(prof + dir, fileName);
            if (file == null) {
                System.out.println("GETTING FILE NOT FOUND");
                return new ResponseEntity("Erro: arquivo do " + prof + " não encontrado.", HttpStatus.NOT_FOUND);
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = file.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            ftp.getFtp().completePendingCommand();
            ftp.disconnect();

            System.out.println("GETTING FILE: SUCCESS!");
            return new ResponseEntity(buffer.toByteArray(), HttpStatus.OK);
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
            if (img == null) {
                System.out.println("GETTING User photo: getFile returns NULL");
                return new ResponseEntity("Erro: img do professor " + prof + " não encontrada.", HttpStatus.NOT_FOUND);
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = img.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            ftp.getFtp().completePendingCommand();
            ftp.disconnect();

            System.out.println("GETTING User photo: SUCCESS!");
            return new ResponseEntity(buffer.toByteArray(), HttpStatus.OK);
        } catch (Exception ex) {
            ftp.disconnect();
            return new ResponseEntity("Erro: " + ex, HttpStatus.BAD_REQUEST);
        }

    }

}
