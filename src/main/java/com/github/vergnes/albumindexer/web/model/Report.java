package com.github.vergnes.albumindexer.web.model;

import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by denis.vergnes on 22/08/2015.
 */
public class Report {
    private final long totalCount;
    private List<Bucket> albumsOverTime = new ArrayList<>();
    private List<Bucket> albumsByGenre = new ArrayList<>();

    public Report(long totalCount, Collection<? extends DateHistogram.Bucket> albumsOverTime,
                  Collection<? extends MultiBucketsAggregation.Bucket> albumsByGenre) {
        this.totalCount = totalCount;
        this.albumsOverTime = albumsOverTime.stream().map(b -> new Bucket<Long>(b.getKeyAsNumber().longValue(),
                b.getDocCount())).collect(toList());
        this.albumsByGenre = albumsByGenre.stream().map(b -> new Bucket<String>(b.getKey(),
                b.getDocCount())).collect(toList());
    }

    public List<Bucket> getAlbumsByGenre() {
        return albumsByGenre;
    }

    public List<Bucket> getAlbumsOverTime() {
        return albumsOverTime;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public static class Bucket<KEY extends Serializable> {
        private KEY key;
        private long count;

        public Bucket(KEY key, long count) {
            this.key = key;
            this.count = count;
        }

        public KEY getKey() {
            return key;
        }

        public long getCount() {
            return count;
        }
    }
}
