package com.mussum.controllers.ftp;

import com.mussum.controllers.FeedController;
import com.mussum.controllers.ftp.utils.FTPcontrol;
import com.mussum.models.ftp.Arquivo;
import com.mussum.models.ftp.Pasta;
import com.mussum.repository.ArquivoRepository;
import com.mussum.repository.FeedRepository;
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
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private ArquivoRepository arqRep;

    @Autowired
    private FeedRepository feedRep;

    private final FTPcontrol ftp = new FTPcontrol();

    @PostMapping()
    @ResponseBody
    public ResponseEntity postRepository(
	    @RequestHeader("dir") String dir,
	    @RequestHeader("name") String name,
	    @RequestHeader("visible") boolean visible
    ) {

	String professor = (String) context.getAttribute("requestUser");
	S.out("request new directory user: " + professor, this);
	S.out("request new directory dir: " + dir, this);
	S.out("request new directory visible: " + visible, this);

	if (professor == null || professor.equals("")) {
	    return new ResponseEntity("Erro. Cade o professor?? ", HttpStatus.BAD_REQUEST);
	}

	try {

	    ftp.connect();
	    ftp.getFtp().makeDirectory(dir + "/" + name + "/");
	    ftp.disconnect();
	    Pasta pasta = new Pasta(dir, name);
	    S.out("new folder name: " + name, this);
	    S.out("new folder dir: " + dir, this);
	    pasta.setVisivel(visible);
	    ftp.connect();
	    pastaRep.save(pasta);

	    S.out("directory created.", this);
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
	    @RequestHeader("dir") String dir
    ) {

	Pasta requestDirectory = new Pasta(dir, username);
	try {
	    //PEGA OS ARQUIVOS E PASTAS QUE TEM NO FTP E ATUALIZA O BANCO DE DADOS
	    ftp.connect();
	    List<Arquivo> ftpArq = getArquivosFromDir(dir);
	    List<Pasta> ftpPasta = getPastasFromDir(dir);
	    ftp.disconnect();

	    //AGORA VERIFICA SE ESSES DADOS BATEM COM O QUE TEM NO BANCO DE DADOS
	    List<Arquivo> dbFiles = arqRep.findByDir(dir);
	    List<Pasta> dbFolders = pastaRep.findByDir(dir);

	    for (int i = 0; i < dbFiles.size(); i++) {
		Arquivo loopFile = dbFiles.get(i);
		if (!ftpArq.contains(loopFile)) {
		    S.out("DB update from FTP: removing file", this);
		    arqRep.delete(loopFile);
		    S.out("removed", this);
		}
	    }

	    S.out("VERYFING PASTAS...", this);

	    for (int i = 0; i < dbFolders.size(); i++) {
		Pasta loopFolder = dbFolders.get(i);
		if (!ftpPasta.contains(loopFolder)) {
		    S.out("DB update from FTP: removing folder", this);
		    pastaRep.delete(loopFolder);
		    S.out("removed", this);
		}
	    }

	    dbFiles = arqRep.findByDir(dir);
	    dbFolders = pastaRep.findByDir(dir);

	    List<Arquivo> resFiles = new ArrayList();
	    List<Pasta> resFolders = new ArrayList();

	    if (context.getAttribute("requestUser") == null || !username.equals((String) context.getAttribute("requestUser"))) {
		S.out("Usuario anonimo ou não proprietário: Retirando items privado do response.", this);
		for (Arquivo file : dbFiles) {
		    if (file.isVisivel()) {
			resFiles.add(file);
		    }
		}
		for (Pasta folder : dbFolders) {
		    if (folder.isVisivel()) {
			resFolders.add(folder);
		    }
		}

	    } else {
		S.out("Usuario proprietário: Retornando  items privados e nao privados", this);
		resFiles = dbFiles;
		resFolders = dbFolders;
	    }

	    requestDirectory.getArquivos().addAll(resFiles);
	    requestDirectory.getPastas().addAll(resFolders);

	    S.out("GET repositorys sucess", this);
	} catch (Exception e) {
	    ftp.disconnect();
	    S.out("ERRO: " + e.getCause().getLocalizedMessage(), this);
	}
	return requestDirectory;
    }

    private List<Pasta> getPastasFromDir(String dir) {
	List<Pasta> pastas = new ArrayList();
	try {
	    ftp.connect();
	    S.out("get folders of: " + dir, this);
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
	    S.out("get files of: " + dir, this);
	    FTPFile[] files = ftp.getFtp().listFiles(dir);
	    ftp.disconnect();

	    for (FTPFile item : files) {
		String fileName = item.getName();
		if (item.isFile()) { //arquivo
		    Arquivo file = null;
		    if (arqRep.findByDirInAndNomeIn(dir, fileName).size() > 0) {
			file = arqRep.findByDirInAndNomeIn(dir, fileName).get(0);
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
			arqRep.save(file);
		    }
		    arquivos.add(file);
		    S.out("Retornando arquivo: " + dir + "/" + fileName, this);
		}
	    }
	} catch (IOException ex) {
	    ftp.disconnect();
	    S.out("ERRO: FTP List Files: " + ex.getMessage(), this);
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

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity putDirectory(
	    @PathVariable("id") Integer id,
	    @RequestBody Map<String, String> body
    ) {
	try {
	    Pasta pasta = pastaRep.getOne(id);
	    String newName = body.get("name");

	    S.out("======================== Alterando FEED...", this);
	    FeedController feedControl = new FeedController(context, feedRep);
	    feedControl.changeOldDirByNewDir(pasta.getDir() + "/" + pasta.getNome(), pasta.getDir() + "/" + newName);
	    S.out("...FEED alterado.", this);

	    ftp.connect();
	    FTPFile[] ftpFiles = ftp.getFtp().listFiles(pasta.getDir());
	    for (FTPFile file : ftpFiles) {
		if (file.getName().equals(pasta.getNome())) {
		    ftp.getFtp().changeWorkingDirectory(pasta.getDir());
		    ftp.getFtp().rename(pasta.getNome(), newName);
		    pasta.setNome(newName);
		    file.setName(newName);
		    break;
		}
	    }
	    ftp.disconnect();

	    pastaRep.save(pasta);
	    return ResponseEntity.ok("Nome da pasta alterado com sucesso.");
	} catch (IOException ex) {
	    S.out("ERRO: " + ex.getMessage(), this);
	    return ResponseEntity.badRequest().build();
	}
    }

}
