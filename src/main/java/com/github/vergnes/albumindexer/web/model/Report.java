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
