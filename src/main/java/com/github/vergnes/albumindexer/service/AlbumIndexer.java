package com.github.vergnes.albumindexer.service;

import com.github.vergnes.albumindexer.domain.Album;
import com.github.vergnes.albumindexer.repository.AlbumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.function.Consumer;

/**
 * Created by denis.vergnes on 22/08/2015.
 */
@Component
public class AlbumIndexer implements Consumer<Album> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Inject
    private AlbumRepository repository;

    @Override
    public void accept(Album album) {
        if (!repository.exists(album.getId())) {
            LOGGER.info("New album to index: {}", album);
            repository.save(album);
        } else {
            LOGGER.debug("Ignore already indexed album: {}", album);
        }
    }
}
