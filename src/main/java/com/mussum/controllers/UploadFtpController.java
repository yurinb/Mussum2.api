package com.mussum.controllers;

import com.mussum.controllers.util.FtpController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UploadFtpController {

    private final FtpController ftp = new FtpController();

    //Multiple file upload
    @PostMapping("/api/ftp/upload")
    public ResponseEntity<?> getFiles(
	    @RequestParam("dir") String dir,
	    @RequestParam("files") MultipartFile[] uploadfiles) throws Exception {

	//Get file name
	String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
		.filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

	if (StringUtils.isEmpty(uploadedFileName)) {
	    return new ResponseEntity("please select a file!", HttpStatus.OK);
	}

	try {

	    uploadToFTP(Arrays.asList(uploadfiles), dir);
	    System.out.println("fez up");
	} catch (IOException e) {
	    System.out.println(e);
	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	return new ResponseEntity("Successfully uploaded - "
		+ uploadedFileName, HttpStatus.OK);

    }

    //save file
    private void uploadToFTP(List<MultipartFile> files, String dir) throws IOException, Exception {
	for (MultipartFile file : files) {
	    System.out.println("recebendo arquivo");
	    if (file.isEmpty()) {
		continue; //next pls
	    }
	    ftp.uploadFile(file.getInputStream(), dir, file.getOriginalFilename());
	    System.out.println("arquivo baixado");
	}

    }
}
