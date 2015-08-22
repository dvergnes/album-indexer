package com.github.vergnes.albumindexer.repository;

import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;

import java.util.Collection;

/**
 * Created by denis.vergnes on 22/08/2015.
 */
public interface ElasticSearchAlbumRepository {

    Collection<? extends DateHistogram.Bucket> aggregateAlbumsOverTime();

    Collection<? extends org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket> aggregateAlbumsByGenre();
}
