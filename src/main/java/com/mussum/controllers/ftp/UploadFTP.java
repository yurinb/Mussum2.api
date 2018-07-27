package com.mussum.controllers.ftp;

import com.mussum.controllers.EmailController;
import com.mussum.controllers.FeedController;
import com.mussum.controllers.SearchController;
import com.mussum.controllers.ftp.utils.FTPcontrol;
import com.mussum.controllers.ftp.utils.Strings;
import com.mussum.models.db.Feed;
import com.mussum.models.db.Professor;
import com.mussum.models.ftp.Arquivo;
import com.mussum.repository.ArquivoRepository;
import com.mussum.repository.FeedRepository;
import com.mussum.repository.FollowerRepository;
import com.mussum.repository.PastaRepository;
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
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    private FollowerRepository follRep;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PastaRepository pastaRep;

    private final EmailController emailController = new EmailController();

    public UploadFTP() {
    }

    public UploadFTP(ArquivoRepository arqRep) {
	this.arqRep = arqRep;
    }

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
	Professor prof = profRep.findByUsername((String) context.getAttribute("requestUser"));

	if (uploadfiles.length == 0) {
	    S.out(":::::::::::::: files lenght == 0", this);
	}

	if (prof == null) {
	    S.out("ERRO: Professor não encontrado.", this);
	    return new ResponseEntity("Erro: Professor não encontrado ", HttpStatus.BAD_REQUEST);
	}

	//Get file name
	String uploadedFiles = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
		.filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

	if (StringUtils.isEmpty(uploadedFiles)) {
	    S.out("uploading a link...", this);
	    TxtWritter wr = new TxtWritter();
	    Arquivo arquivo = new Arquivo(reqDir, fileName + ".link");
	    FTPcontrol ftp = new FTPcontrol();
	    try (InputStream input = wr.getInputStreamOfNewTxtFile(prof.getNome(), fileName, link)) {
		ftp.connect();
		arquivo.setNome(ftp.uploadFile(input, arquivo, false));
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
	    if (visivel && visibleDir(arquivo.getDir())) {
		feedRep.save(new Feed(arquivo, prof));
	    }

	    S.out("Sending email to followers...", this);
	    emailController.sendMailToFollowers(reqDir, prof, arquivo.getNome(), follRep, mailSender);
	    S.out("Email send.", this);
	} else {
	    save(Arrays.asList(uploadfiles), reqDir, fileName, prof, comment, visivel, link);
	}

	S.out("upload complete.", this);
	return new ResponseEntity("Successfully uploaded - "
		+ uploadedFiles, HttpStatus.OK);
    }

    private void save(List<MultipartFile> files, String dir, String fileName, Professor prof, String comment, Boolean visivel, String link) {

	for (MultipartFile file : files) {
	    S.out("recebendo arquivo...", this);
	    if (file.isEmpty()) {
		S.out("ERRO: Empty file", this);
		//continue; //next pls
	    }
	    FTPcontrol ftp = new FTPcontrol();
	    try {
		Arquivo arquivo = new Arquivo(dir, fileName);
		S.out("new arquivo...", this);
		arquivo.setComentario(comment);
		arquivo.setVisivel(visivel);
		arquivo.setLink(link);
		ftp.connect();
		arquivo.setNome(ftp.uploadFile(file.getInputStream(), arquivo, false));
		ftp.disconnect();
		S.out("upload file", this);
		arqRep.save(arquivo);
		if (visivel && visibleDir(arquivo.getDir())) {
		    feedRep.save(new Feed(arquivo, prof));
		}
		S.out("arquivo salvo.", this);
		S.out("Sending email to followers...", this);
		emailController.sendMailToFollowers(dir, prof, arquivo.getNome(), follRep, mailSender);
		S.out("Email send.", this);
	    } catch (Exception e) {
		ftp.disconnect();
		S.out("ERRO: " + e.getMessage(), this);
	    }
	}

    }

    @PostMapping("/api/photo")
    public ResponseEntity setProfessorPhoto(
	    @RequestParam("img") MultipartFile file
    ) {
	String professor = (String) context.getAttribute("requestUser");
	S.out("POSTING User photo " + professor, this);

	if (professor == null || professor.equals("")) {
	    S.out("ERRO: user not logged/found", this);
	    return new ResponseEntity("ERRO: user not logged/found", HttpStatus.BAD_REQUEST);
	}

	String imageExtension = Strings.getExtensionIfExists(file.getOriginalFilename());

	if (imageExtension != null) {
	    FTPcontrol ftp = new FTPcontrol();
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
		ftp.uploadFile(file.getInputStream(), arquivo, true);
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
	FTPcontrol ftp = new FTPcontrol();
	try {
	    S.out("DELETE", this);
	    S.out(dir, this);
	    S.out(name, this);
	    ftp.connect();
	    boolean removeu = ftp.getFtp().deleteFile(dir + "/" + name);
	    ftp.disconnect();
	    if (removeu) {
		Arquivo dbFile = arqRep.findByDirInAndNomeIn(dir, name).get(0);
		arqRep.delete(dbFile);
		FeedController feedControl = new FeedController(context, feedRep);
		feedControl.deleteByDirAndName(dir, name);
	    }
	    return ResponseEntity.ok(dir + " removido > " + removeu);
	} catch (IOException ex) {
	    return ResponseEntity.badRequest().body(" falha ao remover diretorio: " + dir);
	}

    }

    @PutMapping("/api/upload/{id}")
    public ResponseEntity putArquivo(
	    @PathVariable("id") Integer id,
	    @RequestBody Map<String, String> payload
    ) {
	FTPcontrol ftp = new FTPcontrol();
	try {
	    S.out("PUT:", this);
	    Arquivo dbArquivo = arqRep.findById(id).get();
	    S.out("PUT arquivo/link: " + dbArquivo.getNome(), this);
	    FeedController feedControl = new FeedController(context, feedRep);
	    int extensionIndex = dbArquivo.getNome().lastIndexOf(".");
	    String extension = "";
	    if (extensionIndex > 0) {
		extension = dbArquivo.getNome().substring(extensionIndex);
	    }
	    String newName = payload.get("name");
	    if (!newName.endsWith(extension)) {
		newName += extension;
	    }
	    String newLink = payload.get("link");
	    String newComment = payload.get("comment");

	    Feed feed = feedRep.findByDirInAndArquivoIn(dbArquivo.getDir(), dbArquivo.getNome());

	    if (newLink != null) {
		dbArquivo.setLink(newLink);
		feed.setLink(newLink);
	    }
	    if (newComment != null) {
		dbArquivo.setComentario(newComment);
		feed.setComentario(newComment);
	    }
	    feedRep.save(feed);
	    ftp.connect();
	    FTPFile[] ftpFiles = ftp.getFtp().listFiles(dbArquivo.getDir());
	    for (FTPFile file : ftpFiles) {
		if (file.getName().equals(dbArquivo.getNome())) {
		    ftp.getFtp().changeWorkingDirectory(dbArquivo.getDir());
		    ftp.getFtp().rename(dbArquivo.getNome(), newName);
		    feedControl.changeOldFileNameByNewFileName(dbArquivo.getDir(), dbArquivo.getNome(), newName);
		    dbArquivo.setNome(newName);
		    file.setName(newName);
		    break;
		}
	    }
	    ftp.disconnect();
	    arqRep.save(dbArquivo);
	    S.out("PUT arquivo: OK", this);
	    return ResponseEntity.ok(dbArquivo);
	} catch (IOException e) {
	    S.out("ERRO: " + e.getMessage(), this);
	    return ResponseEntity.noContent().build();
	}

    }

    public void changeOldDirByNewDir(String oldDir, String newDir) {
	List<Arquivo> currentFiles = arqRep.findAll();
	currentFiles.forEach((file) -> {
	    if (file.getDir().startsWith(oldDir)) {
		int limiter = oldDir.length();
		String dirAfterLimiter = file.getDir().substring(limiter);
		file.setDir(newDir + dirAfterLimiter);
		arqRep.save(file);
	    }
	});
    }

    private Boolean visibleDir(String dir) {
	SearchController sc = new SearchController();
	return sc.checkPublicDir(dir);
    }

}
