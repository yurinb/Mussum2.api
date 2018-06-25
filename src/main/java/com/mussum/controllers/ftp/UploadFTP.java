package com.mussum.controllers.ftp;

import com.mussum.controllers.ftp.utils.FTPcontrol;
import com.mussum.controllers.ftp.utils.Strings;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.net.ftp.FTPFile;
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

    private final FTPcontrol ftp = new FTPcontrol();

    //Multiple file upload
    @PostMapping("/api/upload")
    public ResponseEntity postFiles(
	    @RequestHeader("dir") String reqDir,
	    @RequestHeader("fileName") String fileName,
	    @RequestHeader("comment") String comment,
	    @RequestHeader("link") String link,
	    @RequestHeader("visible") Boolean visivel,
	    @RequestParam("files") MultipartFile[] uploadfiles
    ) throws Exception {

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
	    Arquivo arquivo = new Arquivo(reqDir, fileName + ".link");
	    try (InputStream input = wr.getInputStreamOfNewTxtFile(professor, fileName, link)) {
		ftp.connect();
		ftp.uploadFile(input, arquivo);
		ftp.disconnect();
		input.close();
		wr.deleteLastCreatedFile();
	    } catch (Exception ex) {
		S.out("ERRO: " + ex.getMessage(), this);
	    }
	    arquivo.setComentario(comment);
	    arquivo.setVisivel(visivel);

	    boolean http = link.startsWith("http://");
	    boolean https = link.startsWith("https://");
	    if (http || https) {
	    } else {
		link = "http://" + link;
	    }

	    arquivo.setLink(link);
	    arqRep.save(arquivo);
	    feedRep.save(new Feed(arquivo, professor, (String) context.getAttribute("requestUser")));
	} else {
	    save(Arrays.asList(uploadfiles), reqDir, fileName, professor, comment, visivel, link);
	}
	S.out("upload complete.", this);
	return new ResponseEntity("Successfully uploaded - "
		+ uploadedFiles, HttpStatus.OK);
    }

//    @PutMapping("/api/upload/{id}")
//    public ResponseEntity putArquivo(
//            @RequestBody Arquivo arquivo,
//            @PathVariable Integer id
//    ) {
//        Arquivo actual = arqRep.getOne(id);
//        updateNotNullArquivo(actual, arquivo);
//        return ResponseEntity.ok(arqRep.save(actual));
//    }
//    private Arquivo updateNotNullArquivo(Arquivo old, Arquivo newOne) {
//        if (newOne.getComentario() != null) {
//            old.setComentario(newOne.getComentario());
//        }
//        if (newOne.getDir() != null) {
//            old.setDir(newOne.getDir());
//        }
//        if (newOne.isVisivel() == false || newOne.isVisivel() == true) {
//            old.setVisivel(newOne.isVisivel());
//        }
//        if (newOne.getNome() != null) {
//            old.setNome(newOne.getNome());
//        }
//        return old;
//    }
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
		feedRep.save(new Feed(arquivo, professor, context.getAttribute("requestUser").toString()));
		S.out("arquivo salvo.", this);
	    } catch (Exception e) {
		ftp.disconnect();
		S.out(e.getMessage(), this);
	    }
	}

    }

    @PostMapping("/api/photo")
    public ResponseEntity setProfessorPhoto(
	    @RequestParam("img") MultipartFile file
    ) {
	String professor = (String) context.getAttribute("requestUser");
	S.out("POSTING User photo " + professor, this);

//        for (String key : Collections.list(context.getHeaders("Content-Type"))) {
//            S.out(key, this);
//        }
	if (professor == null || professor.equals("")) {
	    S.out("ERRO: user not logged/found", this);
	    return new ResponseEntity("ERRO: user not logged/found", HttpStatus.BAD_REQUEST);
	}

	String imageExtension = Strings.getExtensionIfExists(file.getOriginalFilename());

	if (imageExtension != null) {

	    try {
		ftp.connect();

		String[] fotosPerfil = ftp.getContentFrom("\\_res\\perfil_img\\");
		for (String foto : fotosPerfil) {
		    if (foto.startsWith(professor)) {
			String photoFoundName = foto;
			ftp.getFtp().deleteFile("\\_res\\perfil_img\\" + photoFoundName);
		    }
		}
		Arquivo arquivo = new Arquivo("\\_res\\perfil_img\\", professor + imageExtension);
		ftp.uploadFile(file.getInputStream(), arquivo);
		S.out(ftp.getFtp().getReplyString(), this);
		ftp.disconnect();
		return new ResponseEntity("Foto de perfil recebida. ", HttpStatus.CREATED);
	    } catch (Exception ex) {
		ftp.disconnect();
		S.out("ERROR: " + ex.getMessage(), this);
		return new ResponseEntity("Erro no upload FTP: " + ex, HttpStatus.BAD_REQUEST);
	    }
	} else {
	    S.out("ERRO: no extension found in file", this);
	    return new ResponseEntity("ERRO: imagem sem extensão", HttpStatus.BAD_REQUEST);
	}
    }

    @DeleteMapping("/api/upload")
    public ResponseEntity deleteArquivo(
	    @RequestHeader("dir") String dir,
	    @RequestHeader("name") String name
    ) {
	try {
	    ftp.connect();
	    S.out("DELETE", this);
	    S.out(dir, this);
	    S.out(name, this);
	    Arquivo dbFile = arqRep.findByDirInAndNomeIn(dir, name).get(0);
	    arqRep.delete(dbFile);
	    boolean removeu = ftp.getFtp().deleteFile(dir + "/" + name);
	    ftp.disconnect();
	    return ResponseEntity.ok(dir + " removeu? " + removeu);
	} catch (IOException ex) {
	    return ResponseEntity.badRequest().body(" falha ao remover diretorio: " + dir);
	}

    }

    @PutMapping("/api/upload/{id}")
    public ResponseEntity putArquivo(
	    @PathVariable("id") Integer id,
	    @RequestBody Map<String, String> payload
    ) {
	try {
	    S.out("PUT:", this);
	    Arquivo dbArquivo = arqRep.findById(id).get();
	    S.out("PUT arquivo/link: " + dbArquivo.getNome(), this);

	    dbArquivo.setComentario(payload.get("comment"));
	    dbArquivo.setLink(payload.get("link"));
	    ftp.connect();
	    FTPFile[] ftpFiles = ftp.getFtp().listFiles(dbArquivo.getDir());
	    for (FTPFile file : ftpFiles) {
		if (file.getName().equals(dbArquivo.getNome())) {
		    ftp.getFtp().changeWorkingDirectory(dbArquivo.getDir());
		    ftp.getFtp().rename(dbArquivo.getNome(), payload.get("name"));
		    dbArquivo.setNome(payload.get("name"));
		    file.setName(payload.get("name"));
		    break;
		}
	    }
	    ftp.disconnect();
	    arqRep.save(dbArquivo);
	    S.out("PUT arquivo: OK", this);
	    return ResponseEntity.ok(dbArquivo);
	} catch (Exception e) {
	    S.out("ERRO: " + e.getMessage(), this);
	    return ResponseEntity.noContent().build();
	}

    }

}
