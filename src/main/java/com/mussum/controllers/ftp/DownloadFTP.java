package com.mussum.controllers.ftp;

import com.mussum.controllers.ftp.utils.FTPcontrol;
import com.mussum.controllers.ftp.utils.Convert;
import com.mussum.util.S;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.io.InputStream;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class DownloadFTP {

    @GetMapping("/api/download")
    public ResponseEntity getFile(
	    @RequestHeader("professor") String prof,
	    @RequestHeader("dir") String dir,
	    @RequestHeader("fileName") String fileName) {
	FTPcontrol ftp = new FTPcontrol();
	try {

	    S.out("requesting FILE 1: " + dir + "/" + fileName, this);
	   //ftp.getFtp().setSoTimeout(3000);
	    ftp.connect();
	    S.out("requesting FILE 2: " + dir + "/" + fileName, this);
	    InputStream file = ftp.getFile(dir + "/", fileName);
	    S.out("requesting FILE 3: " + dir + "/" + fileName, this);
	    //ftp.getFtp().completePendingCommand();
	    S.out("requesting FILE 4: " + dir + "/" + fileName, this);
	    byte[] bytes = StreamUtils.copyToByteArray(file);
	    ftp.disconnect();
	    S.out("requesting FILE 5: " + dir + "/" + fileName, this);
	    if (file == null) {
		//S.out("ERRO: File not found", this);
		return new ResponseEntity("ERRO: File not found", HttpStatus.NOT_FOUND);
	    }
	    //file.close();
	    S.out("file served.", this);
	    return new ResponseEntity(bytes, HttpStatus.OK);
	} catch (Exception ex) {
	    ftp.disconnect();
	    S.out("ERRO: " + ex.getMessage(), this);
	    return new ResponseEntity("Erro: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

    }

    @GetMapping("/api/photo")
    public ResponseEntity getProfessorPhoto(@RequestHeader("professor") String prof) {
	FTPcontrol ftp = new FTPcontrol();
	try {
	    S.out("requesting user PHOTO: " + prof, this);

	    ftp.connect();
	    String[] fotosPerfil = ftp.getContentFrom("\\_res\\perfil_img\\");
	    ftp.disconnect();

	    String profPhotoName = null;
	    for (String foto : fotosPerfil) {
		S.out("FOTO: " + foto, this);
		if (foto.startsWith(prof)) {
		    profPhotoName = foto;
		    break;
		}
	    }
	    ftp.connect();
	    InputStream img = ftp.getFile("\\_res\\perfil_img\\", profPhotoName);
	    ftp.disconnect();

	    if (img == null) {
		S.out("ERRO: photo " + profPhotoName + " not found", this);
		return new ResponseEntity("ERRO: photo not found", HttpStatus.NOT_FOUND);
	    }

	    String base64 = Convert.inputStreamToBASE64(img);
	    img.close();
	    S.out("user photo served.", this);
	    return new ResponseEntity(base64, HttpStatus.OK);

	} catch (Exception ex) {
	    ftp.disconnect();
	    S.out("ERRO: " + ex.getMessage(), this);
	    return new ResponseEntity("ERRO: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

    }

}
