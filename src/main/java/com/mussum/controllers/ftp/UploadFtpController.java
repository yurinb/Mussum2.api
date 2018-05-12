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
import java.util.stream.Collectors;
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
    public Pasta getRepository(@RequestHeader("username") String username) {

        //List<FtpTempRepository> repos = ftp.getProfFtpRepoList();
        Pasta userRepository = new Pasta(username);

        userRepository.getPastas().addAll(getPastasFromDir(username));
        userRepository.getArquivos().addAll(getArquivosFromDir(username));

        List<String> diretoriosVerificados = new ArrayList();
        List<Pasta> pastasDoScopo = new ArrayList();

        pastasDoScopo.addAll(userRepository.getPastas());
        int scopoIndex = 0;
        int maxScopeIndex = 0;

        while (true) {
            if (estasPastasForamVerificadas(userRepository.getPastas(), diretoriosVerificados)) {
                break;
            }

            Pasta pastaAtual = pastasDoScopo.get(scopoIndex);
            String scopeDir = pastaAtual.getDir();

            if (pastaAtual.getPastas().isEmpty()) {
                //se nao houver pastas na pasta scopo ou se elas ja foram verificadas:
                System.out.println(pastaAtual.getDir() + " > VAZIO");
                diretoriosVerificados.add(scopeDir);
                if (scopoIndex < maxScopeIndex) {
                    scopoIndex++;
                    continue;
                } else {
                    scopoIndex = 0;

                }
            }

            if (estasPastasForamVerificadas(pastasDoScopo, diretoriosVerificados)) {
                System.out.println(pastaAtual.getDir() + " > JA VERIFICADO");
                if (scopoIndex < maxScopeIndex) {
                    scopoIndex++;
                    continue;
                } else {
                    scopoIndex = 0;
                    pastaAtual;
                }
            }
            pastaAtual = pastaAtual.getPastas().maxScopeIndex = pastaAtual.getPastas().size();

            if (diretoriosVerificados.contains(scopeDir)) {
                //se o diretorio da pasta ja foi verificado:
                if (scopoIndex < maxScopeIndex) {
                    scopoIndex++;
                    continue;
                } else {

                }

            } else {
                //se o diretorio da pasta ainda nao foi verificado:
                pastaAtual.getPastas().addAll(getPastasFromDir(scopeDir));
                if (scopoIndex < maxScopeIndex) {
                    scopoIndex++;
                }

            }

        }

        return userRepository;
    }

    private List<Pasta> getPastasFromDir(String dir) {
        String[] scopeContent = ftp.getContentFrom(dir);
        List<Pasta> scopeFolder = new ArrayList();

        for (String item : scopeContent) {
            int splitSize = item.split(".").length; //verificando se existe extensao no nome do item

            if (splitSize == 0) { //pasta
                Pasta folder = new Pasta(item);
                scopeFolder.add(folder);
            }

        }
        return scopeFolder;
    }

    private List<Arquivo> getArquivosFromDir(String dir) {
        String[] scopeContent = ftp.getContentFrom(dir);
        List<Arquivo> arquivos = new ArrayList();

        for (String item : scopeContent) {

            int splitSize = item.split(".").length; //verificando se existe extensao no nome do item

            if (splitSize >= 1) { //arquivo
                Arquivo file = new Arquivo(item);
                arquivos.add(file);

            }
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
