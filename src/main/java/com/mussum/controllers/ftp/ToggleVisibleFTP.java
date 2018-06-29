package com.mussum.controllers.ftp;

import com.mussum.controllers.ftp.utils.FTPcontrol;
import com.mussum.models.db.Feed;
import com.mussum.models.db.Professor;
import com.mussum.models.ftp.Arquivo;
import com.mussum.models.ftp.Pasta;
import com.mussum.repository.ArquivoRepository;
import com.mussum.repository.FeedRepository;
import com.mussum.repository.PastaRepository;
import com.mussum.repository.ProfessorRepository;
import com.mussum.util.S;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class ToggleVisibleFTP {

    private final FTPcontrol ftp = new FTPcontrol();

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ProfessorRepository profRep;

    @Autowired
    private PastaRepository pastaRep;

    @Autowired
    private ArquivoRepository arquivoRep;

    @Autowired
    private FeedRepository feedRep;

    @PostMapping("/api/togglevisible")
    public ResponseEntity toggleVisible(
	    @RequestHeader("fileName") String fileName,
	    @RequestHeader("dir") String dir) {
	try {
	    S.out("request toggle visible: " + dir + "/" + fileName, this);
	    Professor prof = profRep.findByUsername((String) context.getAttribute("requestUser"));

	    ftp.connect();
	    ftp.getFtp().setSoTimeout(3000);
	    boolean fileExist = ftp.checkFileExists(dir + "/" + fileName);
	    boolean dirExists = ftp.checkDirectoryExists(dir + "/");
	    //ftp.getFtp().completePendingCommand();
	    ftp.disconnect();
	    //file.close();
	    if (!fileExist && !dirExists) {
		S.out("ERROR: file not found", this);
		return new ResponseEntity("ERROR: file not found", HttpStatus.NOT_FOUND);
	    }

	    if (fileExist) {
		if (arquivoRep.findByDirInAndNomeIn(dir, fileName).size() > 0) {
		    Arquivo arquivo = arquivoRep.findByDirInAndNomeIn(dir, fileName).get(0);
		    arquivo.setVisivel(!arquivo.isVisivel());
		    arquivoRep.save(arquivo);
		    if (arquivo.isVisivel()) {
			feedRep.save(new Feed(arquivo, prof));
		    }
		    return new ResponseEntity(arquivo.isVisivel(), HttpStatus.OK);
		} else {
		    return new ResponseEntity("file not mapped", HttpStatus.BAD_REQUEST);
		}
	    }

	    if (dirExists) {
		if (pastaRep.findByDirInAndNomeIn(dir, fileName).size() > 0) {
		    Pasta pasta = pastaRep.findByDirInAndNomeIn(dir, fileName).get(0);
		    pasta.setVisivel(!pasta.isVisivel());
		    pastaRep.save(pasta);
		    return new ResponseEntity(pasta.isVisivel(), HttpStatus.OK);
		} else {
		    return new ResponseEntity("folder not mapped", HttpStatus.BAD_REQUEST);
		}
	    }

	    return new ResponseEntity("Deu ruim", HttpStatus.BAD_REQUEST);
	} catch (Exception ex) {
	    ftp.disconnect();
	    S.out("ERRO: " + ex.getMessage(), this);
	    return new ResponseEntity("ERRO: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

    }

}
