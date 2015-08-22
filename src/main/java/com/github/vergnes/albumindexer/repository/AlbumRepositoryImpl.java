package com.github.vergnes.albumindexer.repository;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
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
public class AlbumRepositoryImpl implements ExtendedAlbumRepository {
    public static final String ALBUMS_OVER_TIME = "albums-over-time";
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


}
