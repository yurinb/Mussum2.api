/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mussum.controllers;

import com.mussum.models.UploadModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UploadLocalController {

    private final Logger logger = LoggerFactory.getLogger(UploadLocalController.class);

    //Save the uploaded file to this folder
    private static final String UPLOADED_FOLDER = "C://mussumFileRepository//";

    // 3.1.1 Single file upload
    @PostMapping("/upload")
    // If not @RestController, uncomment this
    //@ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile) {

	if (uploadfile.isEmpty()) {
	    System.out.println("Nenhum 'file' encontrado na requisição!");
	    return new ResponseEntity("Nenhum 'file' encontrado na requisição!", HttpStatus.OK);
	}

	System.out.println("recebendo arquivo...");
	try {
	    saveUploadedFiles(Arrays.asList(uploadfile), "");
	    System.out.println("recebido!");

	} catch (IOException e) {
	    System.out.println("fail..." + e);
	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	return new ResponseEntity("Sucess - "
		+ uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }

    // 3.1.2 Multiple file upload
    @PostMapping("/upload/multi")
    public ResponseEntity<?> uploadFileMulti(
	    @RequestParam("dir") String dir,
	    @RequestParam("files") MultipartFile[] uploadfiles) {

	System.out.println("ba");
	// Get file name

	System.out.println(uploadfiles[0].getName() + uploadfiles[1].getName());

	String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
		.filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

	if (StringUtils.isEmpty(uploadedFileName)) {
	    return new ResponseEntity("please select a file!", HttpStatus.OK);
	}

	try {

	    saveUploadedFiles(Arrays.asList(uploadfiles), dir);

	} catch (IOException e) {
	    System.out.println(e);
	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	return new ResponseEntity("Sucess - "
		+ uploadedFileName, HttpStatus.OK);

    }

    // 3.1.3 maps html form to a Model
    @PostMapping("/upload/multi/model")
    public ResponseEntity<?> multiUploadFileModel(@ModelAttribute UploadModel model) {

	logger.debug("Multiple file upload! With UploadModel");

	try {
	    saveUploadedFiles(Arrays.asList(model.getFiles()), "");
	} catch (IOException ex) {
	    System.out.println("DEU RUIM " + ex.getMessage());
	}

	System.out.println("arquivo recebido.");
	return new ResponseEntity("Successfully uploaded!", HttpStatus.OK);

    }

    //save file
    private void saveUploadedFiles(List<MultipartFile> files, String dir) throws IOException {

	for (MultipartFile file : files) {

	    if (file.isEmpty()) {
		continue; //next pls
	    }

	    byte[] bytes = file.getBytes();
	    try {
		Path path = Paths.get(UPLOADED_FOLDER + dir + file.getOriginalFilename());
		if (!Files.isWritable(path)) {
		    System.out.println("puts");
		}
		Files.write(path, bytes);

	    } catch (Exception e) {
		System.out.println(e);
	    }

	}

    }
}
