package com.mussum.controllers.ftp;

import com.mussum.models.ftp.Arquivo;
import com.mussum.models.ftp.Pasta;
import com.mussum.repository.ProfessorRepository;
import io.jsonwebtoken.lang.Strings;
import java.io.ByteArrayInputStream;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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
    public ResponseEntity<?> getFiles(
            @RequestParam("dir") String reqDir,
            @RequestParam("username") String username,
            @RequestParam("files") MultipartFile[] uploadfiles) throws Exception {

        System.out.println("USERNAME > " + username);

        //Get file name
        String uploadedFiles = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFiles)) {
            return new ResponseEntity("Nenhum arquivo recebido!", HttpStatus.BAD_REQUEST);
        }

        try {
            save(Arrays.asList(uploadfiles), "/" + username + "/" + reqDir + "/");
        } catch (IOException e) {
            System.out.println("Upload FAIL: " + e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - "
                + uploadedFiles, HttpStatus.OK);

    }

    //save file
    private void save(List<MultipartFile> files, String dir) throws IOException, Exception {
        for (MultipartFile file : files) {
            System.out.println("recebendo arquivo...");
            if (file.isEmpty()) {
                continue; //next pls
            }
            ftp.uploadFile(file.getInputStream(), dir, file.getOriginalFilename());
            System.out.println("arquivo salvo.");

        }

    }

    @PostMapping("/api/photo")
    public ResponseEntity<?> setProfessorPhoto(@RequestParam("img") MultipartFile file) {
        String professor = (String) context.getAttribute("requestUser");
        System.out.println("User photo " + professor);

        if (professor == null || professor.equals("")) {
            return new ResponseEntity("Erro. Cade o professor?? ", HttpStatus.BAD_REQUEST);
        }

        try {
            if (file.getOriginalFilename().endsWith(".png")) {

                ftp.uploadFile(file.getInputStream(), "\\_res\\perfil_img\\", professor + ".png");
                return new ResponseEntity("Foto de perfil recebida. ", HttpStatus.CREATED);
            } else {
                return new ResponseEntity("Erro: SOMENTE PNG POR ENQUANTO", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            return new ResponseEntity("Erro: " + ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/photo")
    public ResponseEntity getProfessorPhoto(@RequestHeader("professor") String prof) {

        try {
            System.out.println("User photo " + prof);
            InputStream img = ftp.getFile("\\_res\\perfil_img\\", prof + ".png");
            if (img == null) {
                return new ResponseEntity("Erro: img do professor " + prof + " não encontrada.", HttpStatus.NOT_FOUND);
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = img.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            return new ResponseEntity(buffer.toByteArray(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity("Erro: " + ex, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/api/repository")
    @ResponseBody
    public Pasta getRepository(
            @RequestHeader("username") String username,
            @RequestHeader("dir") String dir) {

        Pasta userRepository = new Pasta(username);

        try {
            userRepository.getArquivos().addAll(getArquivosFromDir(dir));
            userRepository.getPastas().addAll(getPastasFromDir(dir));
        } catch (Exception e) {
            System.out.println(e);
        }

        return userRepository;
    }

    private List<Pasta> getPastasFromDir(String dir) {
        List<Pasta> pastas = new ArrayList();
        try {

            FTPFile[] files = ftp.getFtp().listFiles(dir);

            for (FTPFile item : files) {

                if (item.isDirectory()) { //pasta
                    Pasta folder = new Pasta(dir);
                    pastas.add(folder);
                }
            }
        } catch (IOException ex) {
            System.out.println("ERRO: Pau na listagem de pastas do ftp: " + ex);
        }
        return pastas;
    }

    private List<Arquivo> getArquivosFromDir(String dir) {
        List<Arquivo> arquivos = new ArrayList();
        try {
            FTPFile[] files = ftp.getFtp().listFiles(dir);

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
            System.out.println("ERRO: Pau na listagem de arquivos do ftp: " + ex);
        }
        return arquivos;
    }

    private boolean estasPastasForamVerificadas(List<Pasta> pastas, List<String> verificadas) {
        int size = pastas.size();
        for (int i = 0; i < size; i++) {
            if (!verificadas.contains(pastas.get(i).getDir())) {
                return false;
            }
        }
        return true;
    }

}
