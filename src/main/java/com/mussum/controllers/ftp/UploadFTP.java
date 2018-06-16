package com.mussum.controllers.ftp;

import com.mussum.models.db.Feed;
import com.mussum.models.db.Professor;
import com.mussum.models.ftp.Arquivo;
import com.mussum.repository.ArquivoRepository;
import com.mussum.repository.FeedRepository;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class UploadFTP {

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private FeedRepository feedRep;

    @Autowired
    private ArquivoRepository arqRep;

    private final ControllerFTP ftp = new ControllerFTP();

    //Multiple file upload
    @PostMapping("/api/upload")
    public ResponseEntity postFiles(
            @RequestHeader("dir") String reqDir,
            @RequestHeader("fileName") String fileName,
            @RequestHeader("comment") String comment,
            @RequestHeader("visible") Boolean visivel,
            @RequestParam("files") MultipartFile[] uploadfiles) throws Exception {

        String professor = (String) context.getAttribute("requestUser");
        System.out.println("User upload: " + professor);
        System.out.println("Upload fileName: " + fileName);
        System.out.println("Upload dir: " + reqDir);
        System.out.println("Upload VISIBLE: " + visivel);

        if (professor == null || professor.equals("")) {
            return new ResponseEntity("Erro. Cade o professor?? ", HttpStatus.BAD_REQUEST);
        }

        //Get file name
        String uploadedFiles = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFiles)) {
            return new ResponseEntity("Nenhum arquivo recebido!", HttpStatus.BAD_REQUEST);
        }

        save(Arrays.asList(uploadfiles), reqDir, fileName, professor, comment, visivel);
        return new ResponseEntity("Successfully uploaded - "
                + uploadedFiles, HttpStatus.OK);
    }

    @PutMapping("/api/upload/{id}")
    public ResponseEntity putArquivo(
            @RequestBody Arquivo arquivo,
            @PathVariable Integer id) {
        Arquivo actual = arqRep.getOne(id);
        updateNotNullArquivo(actual, arquivo);
        return ResponseEntity.ok(arqRep.save(actual));
    }

    private Arquivo updateNotNullArquivo(Arquivo old, Arquivo newOne) {
        if (newOne.getComentario() != null) {
            old.setComentario(newOne.getComentario());
        }
        if (newOne.getDir() != null) {
            old.setDir(newOne.getDir());
        }
        if (newOne.isVisivel() == false || newOne.isVisivel() == true) {
            old.setVisivel(newOne.isVisivel());
        }
        if (newOne.getNome() != null) {
            old.setNome(newOne.getNome());
        }
        return old;
    }

    //save file
    private void save(List<MultipartFile> files, String dir, String fileName, String professor, String comment, Boolean visivel) {
        for (MultipartFile file : files) {
            System.out.println("recebendo arquivo...");
            if (file.isEmpty()) {
                System.out.println("file empty");
                continue; //next pls
            }
            try {
                Arquivo arquivo = new Arquivo(dir, fileName);
                System.out.println("new arquivo...");
                arquivo.setComentario(comment);
                arquivo.setVisivel(visivel);
                ftp.connect();
                ftp.uploadFile(file.getInputStream(), arquivo);
                System.out.println("upload ok...");
                ftp.disconnect();
                arqRep.save(arquivo);
                feedRep.save(new Feed(arquivo, professor));
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
                Arquivo arquivo = new Arquivo(professor + ".png", "\\_res\\perfil_img\\");
                ftp.uploadFile(file.getInputStream(), arquivo);
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
