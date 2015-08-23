/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Denis Vergnes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.vergnes.albumindexer.repository;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * Created by denis.vergnes on 22/08/2015.
 */
@Component
public class AlbumRepositoryImpl implements ElasticSearchAlbumRepository {
    public static final String ALBUMS_OVER_TIME = "albums-over-time";
    private static final String ALBUMS_BY_GENRE = "albums-by-genre";
    @Inject
    private ElasticsearchTemplate template;

    @Override
    public Collection<? extends DateHistogram.Bucket> aggregateAlbumsOverTime() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .addAggregation(AggregationBuilders.dateHistogram(ALBUMS_OVER_TIME)
                        .field("releaseDate")
                        .interval(DateHistogram.Interval.DAY))
                .build();
        return template.query(searchQuery, new ResultsExtractor<Collection<? extends DateHistogram.Bucket>>() {
            @Override
            public Collection<? extends DateHistogram.Bucket> extract(SearchResponse response) {
                DateHistogram aggregation = response.getAggregations().get(ALBUMS_OVER_TIME);
                return aggregation.getBuckets();
            }
        });
    }

    @Override
    public Collection<? extends Terms.Bucket> aggregateAlbumsByGenre() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .addAggregation(AggregationBuilders.terms(ALBUMS_BY_GENRE)
                        .field("genre"))
                .build();
        return template.query(searchQuery, new ResultsExtractor<Collection<? extends Terms.Bucket>>
                () {
            @Override
            public Collection<? extends Terms.Bucket> extract(SearchResponse response) {
                Terms aggregation = response.getAggregations().get(ALBUMS_BY_GENRE);
                return aggregation.getBuckets();
            }
        });
    }


}
