package com.mussum.controllers;

import com.mussum.models.db.Feed;
import com.mussum.repository.FeedRepository;
import com.mussum.controllers.ftp.utils.S;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/feed")
public class FeedController {

    private final int PAG_MULTIPLER = 15;

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private FeedRepository feedRepo;

    public FeedController() {
    }

    public FeedController(HttpServletRequest context, FeedRepository feedRepo) {
	this.context = context;
	this.feedRepo = feedRepo;
    }

    @GetMapping()
    public List<Feed> getFeeds() {
	return getAllFeeds(0);
    }

    @GetMapping("/page/{page}")
    public List<Feed> getFeedsPage(@PathVariable Integer page) {
	return getAllFeeds(page);
    }

    private List<Feed> getAllFeeds(int page) {

	List<Feed> feeds = feedRepo.findAll();
	if (feeds.size() < page * PAG_MULTIPLER - PAG_MULTIPLER) {
	    return null;
	}
	Collections.reverse(feeds);

	List<Feed> recados = new ArrayList<>();
	for (int i = 0; i < feeds.size(); i++) {
	    if (feeds.get(i).getTipo().equals("recado") && feeds.get(i).getPriority() > 0) {
		recados.add(feeds.get(i));
	    }
	}

	feeds.removeAll(recados);

	List<Feed> avisos = new ArrayList<>();
	for (int i = 0; i < feeds.size(); i++) {
	    if (feeds.get(i).getTipo().equals("aviso") && feeds.get(i).getPriority() > 0) {
		avisos.add(feeds.get(i));
	    }
	}

	feeds.removeAll(avisos);

	avisos.sort((Feed o1, Feed o2) -> {
	    if (o1.getPriority() > o2.getPriority()) {
		return 1;
	    }
	    if (o1.getPriority() < o2.getPriority()) {
		return -1;
	    }
	    return 0;
	});
	recados.sort((Feed o1, Feed o2) -> {
	    if (o1.getPriority() > o2.getPriority()) {
		return 1;
	    }
	    if (o1.getPriority() < o2.getPriority()) {
		return -1;
	    }
	    return 0;
	});
	// Empilhando na ordem Aviso > Recado > Outros
	avisos.addAll(recados);
	avisos.addAll(feeds);

	if (page == 0) {
	    return avisos;
	} else {
	    int start = page * PAG_MULTIPLER - PAG_MULTIPLER;
	    int ends = page * PAG_MULTIPLER;
	    if (avisos.size() < ends) {
		ends = avisos.size();
	    }
	    return avisos.subList(start, ends);
	}
    }

    @PutMapping("/togglefixed/{id}")
    public ResponseEntity toggleFixed(@PathVariable Integer id) {
	try {
	    Feed reqFeed = feedRepo.findById(id).get();
	    if (reqFeed.getPriority() > 0) {
		reqFeed.setPriority(0);
		feedRepo.save(reqFeed);
	    } else {
		int newPriority = 1;
		List<Feed> allFeeds = feedRepo.findAll();
		for (Feed feed : allFeeds) {
		    if (feed.getTipo().equals(reqFeed.getTipo())) {
			if (feed.getPriority() >= newPriority) {
			    newPriority = feed.getPriority() + 1;
			}
		    }
		}
		reqFeed.setPriority(newPriority);
		feedRepo.save(reqFeed);
	    }
	    return new ResponseEntity(HttpStatus.OK);
	} catch (Exception e) {
	    return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Feed getFeed(@PathVariable Integer id) {
	return feedRepo.findById(id).get();
    }

    @PostMapping()
    @ResponseBody
    public Feed postFeed(@RequestBody @Valid Feed aviso) {
	return feedRepo.save(aviso);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Feed putFeed(@RequestBody Feed newFeed, @PathVariable Integer id) {
	newFeed.setId(feedRepo.findById(id).get().getId());
	feedRepo.save(newFeed);
	return newFeed;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Feed deleteFeed(@PathVariable Integer id) {
	S.out(String.valueOf(id), this);
	Feed feed = feedRepo.findById(id).get();
	S.out("DELETE Feed: " + feed.getUsername() + " : " + feed.getTitulo(), this);
	if (feed.getUsername().equals((String) context.getAttribute("requestUser"))) {
	    feedRepo.delete(feed);
	    S.out("delete Feed: ok", this);
	    return feed;

	}
	S.out("delete Feed: ERRO 404", this);
	return null;
    }

    public void changeOldDirByNewDir(String oldDir, String newDir) {
	List<Feed> currentFeeds = feedRepo.findAll();
	currentFeeds.forEach((feed) -> {
	    if (feed.getDir().startsWith(oldDir)) {
		int limiter = oldDir.length();
		String dirAfterLimiter = feed.getDir().substring(limiter);
		feed.setDir(newDir + dirAfterLimiter);
		feedRepo.save(feed);
	    }
	});
    }

    public void changeOldFileNameByNewFileName(String dir, String oldName, String newName) {
	Feed currentFeed = feedRepo.findByDirInAndArquivoIn(dir, oldName);
	currentFeed.setArquivo(newName);
	feedRepo.save(currentFeed);
    }

    public void deleteAllByDir(String dir) {
	List<Feed> currentFeeds = feedRepo.findAll();
	currentFeeds.forEach((feed) -> {
	    if (feed.getDir().startsWith(dir)) {
		feedRepo.delete(feed);
	    }
	});
    }

    public void deleteByDirAndName(String dir, String name) {
	List<Feed> currentFeed = feedRepo.findAll();
	for (Feed feed : currentFeed) {
	    if (feed.getDir().equals(dir)) {
		if (feed.getArquivo().startsWith(name)) {
		    feedRepo.delete(feed);
		    break;
		}
	    }
	}
    }

}
