package com.mussum.controllers.ftp;

import com.mussum.models.ftp.Arquivo;
import com.mussum.models.ftp.Pasta;
import com.mussum.repository.ProfessorRepository;
import java.io.ByteArrayOutputStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class UploadFtpController {

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ProfessorRepository profRep;

    private final FtpController ftp = new FtpController();

    //Multiple file upload
    @PostMapping("/api/upload")
    public ResponseEntity getFiles(
            @RequestParam("dir") String reqDir,
            @RequestParam("files") MultipartFile[] uploadfiles) throws Exception {

        String professor = (String) context.getAttribute("requestUser");
        System.out.println("User photo " + professor);

        if (professor == null || professor.equals("")) {
            return new ResponseEntity("Erro. Cade o professor?? ", HttpStatus.BAD_REQUEST);
        }

        //Get file name
        String uploadedFiles = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFiles)) {
            return new ResponseEntity("Nenhum arquivo recebido!", HttpStatus.BAD_REQUEST);
        }

        save(Arrays.asList(uploadfiles), "/" + professor + "/" + reqDir + "/");

        return new ResponseEntity("Successfully uploaded - "
                + uploadedFiles, HttpStatus.OK);

    }

    //save file
    private void save(List<MultipartFile> files, String dir) {
        for (MultipartFile file : files) {
            System.out.println("recebendo arquivo...");
            if (file.isEmpty()) {
                continue; //next pls
            }
            try {
                ftp.connect();
                ftp.uploadFile(file.getInputStream(), dir, file.getOriginalFilename());
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

    @PostMapping("/api/repository")
    @ResponseBody
    public ResponseEntity postRepository(
            @RequestHeader("dir") String dir) {

        String professor = (String) context.getAttribute("requestUser");
        System.out.println("User make directory " + professor);

        if (professor == null || professor.equals("")) {
            return new ResponseEntity("Erro. Cade o professor?? ", HttpStatus.BAD_REQUEST);
        }

        try {

            ftp.connect();

            ftp.getFtp().makeDirectory(professor + "/" + dir);
            System.out.println("Diretorio criado.");

            ftp.disconnect();
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (IOException ex) {
            ftp.disconnect();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/repository")
    @ResponseBody
    public Pasta getRepository(
            @RequestHeader("username") String username,
            @RequestHeader("dir") String dir) {

        Pasta userRepository = new Pasta(username);
        try {
            ftp.connect();
            userRepository.getArquivos().addAll(getArquivosFromDir(username + "/" + dir));
            userRepository.getPastas().addAll(getPastasFromDir(username, dir));
            ftp.disconnect();
        } catch (Exception e) {
            ftp.disconnect();
            System.out.println(e);
        }
        return userRepository;
    }

    private List<Pasta> getPastasFromDir(String username, String dir) {
        List<Pasta> pastas = new ArrayList();
        try {
            ftp.connect();

            FTPFile[] files = ftp.getFtp().listFiles(username + "/" + dir);
            ftp.disconnect();

            for (FTPFile item : files) {
                System.out.println("GETTING PASTAS: " + item);
                if (item.isDirectory()) { //pasta
                    Pasta folder = new Pasta(dir + "/" + item.getName());
                    pastas.add(folder);
                }
            }

        } catch (IOException ex) {
            ftp.disconnect();
            System.out.println("ERRO: Pau na listagem de pastas do ftp: " + ex);
        }
        return pastas;
    }

    private List<Arquivo> getArquivosFromDir(String dir) {
        List<Arquivo> arquivos = new ArrayList();
        try {
            ftp.connect();

            FTPFile[] files = ftp.getFtp().listFiles(dir);
            ftp.disconnect();

            for (FTPFile item : files) {
                String fileName = item.getName();

                if (item.isFile()) { //arquivo
                    Arquivo file = new Arquivo(fileName);
                    file.setDir(dir);
                    file.setBytes(item.getSize());
                    arquivos.add(file);
                }
            }
        } catch (IOException ex) {
            ftp.disconnect();
            System.out.println("ERRO: Pau na listagem de arquivos do ftp: " + ex);
        }

        return arquivos;
    }

}
