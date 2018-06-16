package com.mussum.controllers.ftp;

import com.mussum.models.db.Professor;
import com.mussum.models.ftp.Arquivo;
import com.mussum.models.ftp.Pasta;
import com.mussum.repository.ArquivoRepository;
import com.mussum.repository.PastaRepository;
import com.mussum.repository.ProfessorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class ToggleVisibleFTP {

    private final ControllerFTP ftp = new ControllerFTP();

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ProfessorRepository profRep;

    @Autowired
    private PastaRepository pastaRep;

    @Autowired
    private ArquivoRepository arquivoRep;

    @PostMapping("/api/togglevisible")
    public ResponseEntity toggleVisible(
            @RequestHeader("fileName") String fileName,
            @RequestHeader("dir") String dir) {
        try {
            Professor prof = profRep.findByUsername((String) context.getAttribute("requestUser"));
            System.out.println("GETTING Toggle ITEM FROM:" + (String) context.getAttribute("requestUser"));
            System.out.println(prof.getNome());
            System.out.println("GETTING Toggle ITEM:");
            System.out.println(prof.getNome() + dir + fileName);

            ftp.connect();
            ftp.getFtp().setSoTimeout(3000);
            boolean fileExist = ftp.checkFileExists(dir + "/" + fileName);
            boolean dirExists = ftp.checkDirectoryExists(dir + "/");
            //ftp.getFtp().completePendingCommand();
            ftp.disconnect();
            //file.close();
            if (!fileExist && !dirExists) {
                System.out.println("FILE NOT FOUND");
                return new ResponseEntity("Erro: arquivo do " + prof + " não encontrado.", HttpStatus.NOT_FOUND);
            }

            if (fileExist) {
                if (arquivoRep.findByDirInAndNomeIn(dir, fileName).size() > 0) {
                    Arquivo arquivo = arquivoRep.findByDirInAndNomeIn(dir, fileName).get(0);
                    arquivo.setVisivel(!arquivo.isVisivel());
                    arquivoRep.save(arquivo);
                    return new ResponseEntity(arquivo.isVisivel(), HttpStatus.OK);
                } else {
                    return new ResponseEntity("ARQUIVO NAO MAPEADO", HttpStatus.BAD_REQUEST);
                }
            }

            if (dirExists) {
                if (pastaRep.findByDirInAndNomeIn(dir, fileName).size() > 0) {
                    Pasta pasta = pastaRep.findByDirInAndNomeIn(dir, fileName).get(0);
                    pasta.setVisivel(!pasta.isVisivel());
                    pastaRep.save(pasta);
                    return new ResponseEntity(pasta.isVisivel(), HttpStatus.OK);
                } else {
                    return new ResponseEntity("PASTA NAO MAPEADA", HttpStatus.BAD_REQUEST);
                }
            }

            return new ResponseEntity("Deu ruim", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ftp.disconnect();
            System.out.println(ex.getLocalizedMessage());
            return new ResponseEntity("Erro: " + ex, HttpStatus.BAD_REQUEST);
        }

    }

}
