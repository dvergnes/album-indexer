package com.github.vergnes.albumindexer.web.model;

import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by denis.vergnes on 22/08/2015.
 */
public class Report {
    private final long totalCount;
    private List<Bucket> buckets = new ArrayList<>();

    public Report(long totalCount, Collection<? extends DateHistogram.Bucket> buckets) {
        this.totalCount = totalCount;
        this.buckets = buckets.stream().map(b -> new Bucket(b.getKeyAsNumber().longValue(),
                b.getDocCount())).collect(toList());
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public static class Bucket {
        private long key;
        private long count;

        public Bucket(long key, long count) {
            this.key = key;
            this.count = count;
        }

        public long getKey() {
            return key;
        }

        public long getCount() {
            return count;
        }
    }
}
