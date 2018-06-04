package com.mussum.controllers.ftp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class UploadFTP {

    @Autowired
    private HttpServletRequest context;

    private final ControllerFTP ftp = new ControllerFTP();

    //Multiple file upload
    @PostMapping("/api/upload")
    public ResponseEntity postFiles(
            @RequestHeader("dir") String reqDir,
            @RequestHeader("fileName") String fileName,
            @RequestParam("files") MultipartFile[] uploadfiles) throws Exception {

        String professor = (String) context.getAttribute("requestUser");
        System.out.println("User upload: " + professor);
        System.out.println("Upload fileName: " + fileName);
        System.out.println("Upload dir: " + reqDir);

        if (professor == null || professor.equals("")) {
            return new ResponseEntity("Erro. Cade o professor?? ", HttpStatus.BAD_REQUEST);
        }

        //Get file name
        String uploadedFiles = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFiles)) {
            return new ResponseEntity("Nenhum arquivo recebido!", HttpStatus.BAD_REQUEST);
        }

        save(Arrays.asList(uploadfiles), "/" + professor + "/" + reqDir + "/", fileName);

        return new ResponseEntity("Successfully uploaded - "
                + uploadedFiles, HttpStatus.OK);
    }

    //save file
    private void save(List<MultipartFile> files, String dir, String fileName) {
        for (MultipartFile file : files) {
            System.out.println("recebendo arquivo...");
            if (file.isEmpty()) {
                continue; //next pls
            }
            try {
                ftp.connect();
                ftp.uploadFile(file.getInputStream(), dir, fileName);
                ftp.disconnect();
                System.out.println("arquivo salvo.");
            } catch (Exception e) {
                ftp.disconnect();
                System.out.println(e);
            }
        }

    }

    @PostMapping("/api/photo")
    public ResponseEntity setProfessorPhoto(@RequestParam("img") MultipartFile file) {
        String professor = (String) context.getAttribute("requestUser");
        System.out.println("POSTING User photo " + professor);

        for (String key : Collections.list(context.getHeaders("Content-Type"))) {
            System.out.println(key);
        }

        if (professor == null || professor.equals("")) {
            return new ResponseEntity("Erro. Cade o professor?? ", HttpStatus.BAD_REQUEST);
        }

        if (file.getOriginalFilename().endsWith(".png")) {

            try {
                ftp.connect();
                ftp.uploadFile(file.getInputStream(), "\\_res\\perfil_img\\", professor + ".png");
                ftp.getFtp().completePendingCommand();
                ftp.disconnect();
                return new ResponseEntity("Foto de perfil recebida. ", HttpStatus.CREATED);
            } catch (Exception ex) {
                ftp.disconnect();
                return new ResponseEntity("Erro no upload FTP: " + ex, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity("Erro: SOMENTE PNG POR ENQUANTO", HttpStatus.BAD_REQUEST);
        }
    }

    
}
