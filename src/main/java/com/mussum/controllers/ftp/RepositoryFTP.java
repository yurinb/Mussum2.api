package com.mussum.controllers.ftp;

import com.mussum.models.ftp.Arquivo;
import com.mussum.models.ftp.Pasta;
import com.mussum.repository.ArquivoRepository;
import com.mussum.repository.PastaRepository;
import com.mussum.util.S;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping("/api/repository")
public class RepositoryFTP {

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private PastaRepository pastaRep;

    @Autowired
    private ArquivoRepository arquivoRep;

    private final ControllerFTP ftp = new ControllerFTP();

    @PostMapping()
    @ResponseBody
    public ResponseEntity postRepository(
            @RequestHeader("dir") String dir,
            @RequestHeader("name") String name,
            @RequestHeader("visible") boolean visible) {

        String professor = (String) context.getAttribute("requestUser");
        S.out("User make directory " + professor, this);
        S.out("Visible directory " + visible, this);
        S.out("Dir of new directory: " + dir, this);

        if (professor == null || professor.equals("")) {
            return new ResponseEntity("Erro. Cade o professor?? ", HttpStatus.BAD_REQUEST);
        }

        try {

            ftp.connect();
            ftp.getFtp().makeDirectory(dir + "/" + name + "/");
            ftp.disconnect();
            Pasta pasta = new Pasta(dir, name);
            S.out("new pasta name: " + name, this);
            S.out("new pasta dir: " + dir, this);
            pasta.setVisivel(visible);
            ftp.connect();
            pastaRep.save(pasta);

            S.out("Diretorio criado", this);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (IOException ex) {
            ftp.disconnect();
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    @ResponseBody
    public Pasta getRepository(
            @RequestHeader("username") String username,
            @RequestHeader("dir") String dir) {

        Pasta requestDirectory = new Pasta(dir, username);
        try {
            ftp.connect();
            requestDirectory.getArquivos().addAll(getArquivosFromDir(dir));
            requestDirectory.getPastas().addAll(getPastasFromDir(dir));
            ftp.disconnect();
        } catch (Exception e) {
            ftp.disconnect();
            S.out(e.getMessage(), this);
        }
        return requestDirectory;
    }

    private List<Pasta> getPastasFromDir(String dir) {
        List<Pasta> pastas = new ArrayList();
        try {
            ftp.connect();
            S.out("List folders: " + dir, this);
            FTPFile[] files = ftp.getFtp().listFiles(dir);
            ftp.disconnect();

            for (FTPFile item : files) {
                if (item.isDirectory()) { //pasta
                    Pasta folder = null;
                    if (pastaRep.findByDirInAndNomeIn(dir, item.getName()).size() > 0) {
                        folder = pastaRep.findByDirInAndNomeIn(dir, item.getName()).get(0);

                    } else {
                        folder = new Pasta(dir, item.getName());
                        pastaRep.save(folder);
                    }
                    pastas.add(folder);
                    S.out("Retornando pasta: " + folder.getDir() + "/" + folder.getNome(), this);
                }
            }

        } catch (IOException ex) {
            ftp.disconnect();
            S.out("ERRO: FTP List Folders: " + ex.getMessage(), this);
        }
        return pastas;
    }

    private List<Arquivo> getArquivosFromDir(String dir) {
        List<Arquivo> arquivos = new ArrayList();
        try {
            ftp.connect();
            S.out("List files: " + dir, this);
            FTPFile[] files = ftp.getFtp().listFiles(dir);
            ftp.disconnect();

            for (FTPFile item : files) {
                String fileName = item.getName();
                if (item.isFile()) { //arquivo
                    Arquivo file = null;
                    if (arquivoRep.findByDirInAndNomeIn(dir, fileName).size() > 0) {
                        file = arquivoRep.findByDirInAndNomeIn(dir, fileName).get(0);
                    } else {
                        file = new Arquivo(dir, fileName);
                        if (fileName.endsWith(".link")) {
                            ftp.connect();
                            InputStream linkFile = ftp.getFtp().retrieveFileStream(dir + "/" + fileName);
                            ftp.disconnect();
                            byte[] bytes = StreamUtils.copyToByteArray(linkFile);
                            String content = new String(bytes);
                            if (!content.startsWith("http://")) {
                                content = "http://" + content;
                            }
                            file.setLink(content);
                        }
                        arquivoRep.save(file);
                    }
                    arquivos.add(file);
                    S.out("Retornando arquivo: " + dir, this);
                }
            }
        } catch (IOException ex) {
            ftp.disconnect();
            S.out("ERRO: FTP List Files: "+ex.getMessage(), this);
        }

        return arquivos;
    }

    @DeleteMapping
    public ResponseEntity deleteRepo(
            @RequestHeader("dir") String dir,
            @RequestHeader("name") String name) {
        try {
            ftp.connect();
            clean(dir + "/" + name);
            boolean removeu = ftp.getFtp().removeDirectory(dir + "/" + name);
            ftp.disconnect();
            return ResponseEntity.ok(dir + " removeu? " + removeu);
        } catch (IOException ex) {
            return ResponseEntity.badRequest().body(" falha ao remover diretorio: " + dir);
        }

    }

    public void clean(String dir) {
        try {
            FTPFile[] files = ftp.getFtp().listFiles(dir);

            if (files != null) {
                for (FTPFile f : files) {
                    if (f.isDirectory()) {
                        boolean empty = ftp.getFtp().removeDirectory(dir + "/" + f.getName());
                        if (!empty) {
                            clean(dir + "/" + f.getName());
                        }
                    } else {
                        ftp.getFtp().deleteFile(dir + "/" + f.getName());
                    }
                }
            }

        } catch (IOException ex) {
            S.out(ex.getMessage(), this);
        }

    }

}
