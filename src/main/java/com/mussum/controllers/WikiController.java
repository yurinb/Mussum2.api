package com.mussum.controllers;

import com.mussum.models.db.Feed;
import com.mussum.models.db.Wiki;
import com.mussum.repository.FeedRepository;
import com.mussum.repository.ProfessorRepository;
import com.mussum.repository.WikiRepository;
import com.mussum.controllers.ftp.utils.S;
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
@RequestMapping("/api/wikis")
public class WikiController {

    @Autowired
    private WikiRepository wikiRep;

    @Autowired
    private FeedRepository feedRep;

    @Autowired
    private ProfessorRepository profRep;

    @Autowired
    private HttpServletRequest request;

    @GetMapping()
    @ResponseBody
    //@JsonIgnore
    public List<Wiki> getWikis() {
	return wikiRep.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Wiki getWiki(@PathVariable Integer id) {
	return wikiRep.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Wiki postWiki(@RequestBody @Valid Wiki wiki) {
	feedRep.save(new Feed(wiki, profRep.findByUsername(request.getAttribute("requestUser").toString())));
	return wikiRep.save(wiki);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Wiki putWiki(@RequestBody Wiki newWiki, @PathVariable Integer id) {
	Wiki old = wikiRep.findById(id).get();
	newWiki.setId(old.getId());
	wikiRep.save(newWiki);

	try {
	    List<Feed> oldFeeds = feedRep.findAllByTipoInAndTituloIn("wiki", old.getTitulo());
	    Feed oldFeed = oldFeeds.get(0);
	    Feed update = new Feed(newWiki, profRep.findByUsername(oldFeed.getUsername()));
	    update.setId(oldFeed.getId());
	    feedRep.save(update);
	} catch (Exception e) {
	    // S.out("Feed nao econtrado", this);
	}

	return newWiki;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Wiki deleteWiki(@PathVariable Integer id) {
	Wiki wiki = wikiRep.findById(id).get();
	wikiRep.delete(wiki);

	try {
	    List<Feed> oldFeeds = feedRep.findAllByTipoInAndTituloIn("wiki", wiki.getTitulo());
	    Feed oldFeed = oldFeeds.get(0);
	    feedRep.delete(oldFeed);
	} catch (Exception e) {
	    // S.out("Feed nao econtrado", this);
	}

	return wiki;
    }

}
