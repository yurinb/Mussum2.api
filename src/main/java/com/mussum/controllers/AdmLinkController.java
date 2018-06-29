package com.mussum.controllers;

import com.mussum.models.db.AdmLink;
import com.mussum.repository.AdmLinkRepository;
import com.mussum.repository.FeedRepository;
import com.mussum.repository.ProfessorRepository;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admlinks")
public class AdmLinkController {

    @Autowired
    private AdmLinkRepository linkRep;
    
    @Autowired
    private FeedRepository feedRep;
    
    @Autowired
    private ProfessorRepository profRep;
    
    @Autowired
    private HttpServletRequest request;

    @GetMapping()
    @ResponseBody
    //@JsonIgnore
    public List<AdmLink> getLinks() {
	return linkRep.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public AdmLink getLink(@PathVariable Integer id) {
	return linkRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public AdmLink postLink(@RequestBody @Valid AdmLink link) {
	return linkRep.save(link);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public AdmLink putLink(@RequestBody AdmLink link, @PathVariable Integer id) {
	link.setId(linkRep.findById(id).get().getId());
	linkRep.save(link);
	return link;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public AdmLink deleteLink(@PathVariable Integer id) {
	AdmLink link = linkRep.findById(id).get();
	linkRep.delete(link);
	return link;
    }

}
