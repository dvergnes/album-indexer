package com.github.vergnes.albumindexer.repository;

import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;

import java.util.Collection;

/**
 * Created by denis.vergnes on 22/08/2015.
 */
public interface ExtendedAlbumRepository {

    Collection<? extends DateHistogram.Bucket> aggregateAlbumsOverTime();
}
