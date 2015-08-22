package com.github.vergnes.albumindexer.repository;

import com.github.vergnes.albumindexer.domain.Album;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 * Created by denis.vergnes on 22/08/2015.
 */
public interface AlbumRepository extends ElasticsearchCrudRepository<Album, String>, ElasticSearchAlbumRepository {
}
