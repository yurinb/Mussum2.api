package com.mussum.controllers;

import com.mussum.models.ftp.Arquivo;
import com.mussum.models.ftp.Pasta;
import com.mussum.repository.ArquivoRepository;
import com.mussum.repository.PastaRepository;
import com.mussum.repository.ProfessorRepository;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/search")
public class SearchController {

    @Autowired
    ArquivoRepository filesRep;

    @Autowired
    PastaRepository foldersRep;

    @Autowired
    ProfessorRepository profRep;

    /**
     *
     * @param txt
     * @return
     */
    @GetMapping
    public List<SearchObj> searchBy(@RequestHeader("txt") String txt) {

	List<SearchObj> objsFound = new ArrayList();

	List<Arquivo> files = filesRep.findAll();
	for (Arquivo file : files) {
	    if (file.getDir().equals("_res")) {
		continue;
	    }
	    if (file.getNome().toLowerCase().startsWith(txt.toLowerCase())) {
		SearchObj obj = new SearchObj();

		obj.setName(file.getNome());
		obj.setDir(file.getDir());
		obj.setDate(file.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		obj.setType("file");
		String profUsername;
		String professorName;
		try {
		    profUsername = file.getDir().split("/")[0];
		    professorName = profRep.findByUsername(profUsername).getNome();
		} catch (Exception e) {
		    continue;
		}
		obj.setProfessor(professorName);

		objsFound.add(obj);
	    }
	}
	List<Pasta> folders = foldersRep.findAll();
	for (Pasta folder : folders) {
	    if (folder.getNome().toLowerCase().startsWith(txt.toLowerCase())) {
		SearchObj obj = new SearchObj();

		obj.setName(folder.getNome());
		obj.setDir(folder.getDir());
		obj.setDate("");
		obj.setType("folder");
		String profUsername;
		String professorName;
		try {
		    profUsername = folder.getDir().split("/")[0];
		    professorName = profRep.findByUsername(profUsername).getNome();
		} catch (Exception e) {
		    continue;
		}
		obj.setProfessor(professorName);

		objsFound.add(obj);
	    }
	}
	return objsFound;
    }

    private class SearchObj {

	String name;
	String dir;
	String professor;
	String date;
	String type;

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getDir() {
	    return dir;
	}

	public void setDir(String dir) {
	    this.dir = dir;
	}

	public String getProfessor() {
	    return professor;
	}

	public void setProfessor(String professor) {
	    this.professor = professor;
	}

	public String getDate() {
	    return date;
	}

	public void setDate(String date) {
	    this.date = date;
	}

	public String getType() {
	    return type;
	}

	public void setType(String type) {
	    this.type = type;
	}

    }

}
