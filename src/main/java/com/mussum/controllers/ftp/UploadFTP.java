package com.mussum.controllers.ftp;

import com.mussum.models.db.Feed;
import com.mussum.models.ftp.Arquivo;
import com.mussum.repository.ArquivoRepository;
import com.mussum.repository.FeedRepository;
import com.mussum.repository.ProfessorRepository;
import com.mussum.util.S;
import com.mussum.util.TxtWritter;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @Autowired
    private ProfessorRepository profRep;

    private final ControllerFTP ftp = new ControllerFTP();

    //Multiple file upload
    @PostMapping("/api/upload")
    public ResponseEntity postFiles(
            @RequestHeader("dir") String reqDir,
            @RequestHeader("fileName") String fileName,
            @RequestHeader("comment") String comment,
            @RequestHeader("link") String link,
            @RequestHeader("visible") Boolean visivel,
            @RequestParam("files") MultipartFile[] uploadfiles) throws Exception {

        String professor = profRep.findByUsername((String) context.getAttribute("requestUser")).getNome();

        if (professor == null || professor.equals("")) {
            return new ResponseEntity("Erro. Cade o professor?? ", HttpStatus.BAD_REQUEST);
        }

        //Get file name
        String uploadedFiles = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

//        if (StringUtils.isEmpty(uploadedFiles)) {
//            return new ResponseEntity("Nenhum arquivo recebido!", HttpStatus.BAD_REQUEST);
//        }
        if (StringUtils.isEmpty(uploadedFiles)) {
            S.out("uploading a link...", this);
            TxtWritter wr = new TxtWritter();
            ftp.connect();
            Arquivo arquivo = new Arquivo(reqDir, fileName + ".link");
            InputStream input = wr.writeNewTxt(professor, fileName, link);
            ftp.uploadFile(input, arquivo);
            ftp.disconnect();
            arquivo.setComentario(comment);
            arquivo.setVisivel(visivel);
            if (!link.startsWith("http://")) {
                link = "http://" + link;
            }
            arquivo.setLink(link);
            arqRep.save(arquivo);
            feedRep.save(new Feed(arquivo, professor));
        } else {
            save(Arrays.asList(uploadfiles), reqDir, fileName, professor, comment, visivel, link);
        }
        S.out("upload complete.", this);
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
    private void save(List<MultipartFile> files, String dir, String fileName, String professor, String comment, Boolean visivel, String link) {

        for (MultipartFile file : files) {
            S.out("recebendo arquivo...", this);
            if (file.isEmpty()) {
                S.out("empty file", this);
                continue; //next pls
            }
            try {
                Arquivo arquivo = new Arquivo(dir, fileName);
                S.out("new arquivo...", this);
                arquivo.setComentario(comment);
                arquivo.setVisivel(visivel);
                arquivo.setLink(link);
                ftp.connect();
                ftp.uploadFile(file.getInputStream(), arquivo);
                ftp.disconnect();
                S.out("upload file", this);
                arqRep.save(arquivo);
                feedRep.save(new Feed(arquivo, professor));
                S.out("arquivo salvo.", this);
            } catch (Exception e) {
                ftp.disconnect();
                S.out(e.getMessage(), this);
            }
        }

    }

    @PostMapping("/api/photo")
    public ResponseEntity setProfessorPhoto(@RequestParam("img") MultipartFile file) {
        String professor = (String) context.getAttribute("requestUser");
        S.out("POSTING User photo " + professor, this);

        for (String key : Collections.list(context.getHeaders("Content-Type"))) {
            S.out(key, this);
        }

        if (professor == null || professor.equals("")) {
            S.out("ERRO: user not logged/found", this);
            return new ResponseEntity("ERRO: user not logged/found", HttpStatus.BAD_REQUEST);
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

    @DeleteMapping
    public ResponseEntity deleteArquivo(
            @RequestHeader("dir") String dir,
            @RequestHeader("name") String name) {
        try {
            ftp.connect();
            S.out("DELETE", this);
            S.out(dir, this);
            S.out(name, this);
            boolean removeu = ftp.getFtp().deleteFile(dir + "/" + name);
            ftp.disconnect();
            return ResponseEntity.ok(dir + " removeu? " + removeu);
        } catch (IOException ex) {
            return ResponseEntity.badRequest().body(" falha ao remover diretorio: " + dir);
        }

    }

}
