package com.mussum.controllers;

import com.mussum.models.db.Feed;
import com.mussum.repository.FeedRepository;
import com.mussum.util.S;
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
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private FeedRepository feedRepo;

    @GetMapping()
    @ResponseBody
    //@JsonIgnore
    public List<Feed> getFeeds() {
        return feedRepo.findAll();
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
    public Feed deleteAviso(@PathVariable Integer id) {
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

}
