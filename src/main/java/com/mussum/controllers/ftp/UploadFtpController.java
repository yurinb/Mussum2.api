package com.mussum.controllers.ftp;

import com.mussum.models.ftp.Arquivo;
import com.mussum.models.ftp.Pasta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class UploadFtpController {

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
