package com.github.vergnes.albumindexer.web;

import com.github.vergnes.albumindexer.domain.Album;
import com.github.vergnes.albumindexer.repository.AlbumRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by denis.vergnes on 23/08/2015.
 */
@RestController
@RequestMapping("/api")
public class AlbumController {


    @Inject
    private AlbumRepository repository;

    @RequestMapping("/album")
    public Album getAlbum(String id) {
        return repository.findOne(id);
    }

    @RequestMapping("/albums")
    public Iterable<Album> getAlbums() {
        return repository.findAll();
    }


}
