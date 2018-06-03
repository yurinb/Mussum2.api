package com.mussum.controllers.ftp;

import com.mussum.models.ftp.Arquivo;
import com.mussum.models.ftp.Pasta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class RepositoryFTP {

    @Autowired
    private HttpServletRequest context;

    private final ControllerFTP ftp = new ControllerFTP();

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

        Pasta requestDirectory = new Pasta(dir, username);
        try {
            ftp.connect();
            requestDirectory.getArquivos().addAll(getArquivosFromDir(username + "/" + dir));
            requestDirectory.getPastas().addAll(getPastasFromDir(username, dir));
            ftp.disconnect();
        } catch (Exception e) {
            ftp.disconnect();
            System.out.println(e);
        }
        return requestDirectory;
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
                    Pasta folder = new Pasta(dir + "/" + item.getName(), item.getName());
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
                    //file.setBytes(item.getSize());
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
