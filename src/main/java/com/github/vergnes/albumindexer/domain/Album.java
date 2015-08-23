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

package com.github.vergnes.albumindexer.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;
import java.util.Optional;

/**
 * Created by denis.vergnes on 21/08/2015.
 */
@Document(indexName = "albums", type = "album", shards = 1, replicas = 0, refreshInterval = "-1",
        indexStoreType = "niofs")
public class Album {

    @Id
    private String id;
    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true
    )
    private String title;
    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true
    )
    private String artist;
    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed,
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true
    )
    private String genre;
    @Field(
            type = FieldType.Date,
            index = FieldIndex.not_analyzed,
            store = true,
            format = DateFormat.custom, pattern = "dd.MM.yyyy hh:mm"
    )
    private Date releaseDate;


    protected Album() {
    }

    public String getId() {
        return id;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("artist", artist)
                .append("genre", genre)
                .append("releaseDate", releaseDate)
                .toString();
    }

    public static class AlbumBuilder {
        private Album subject = new Album();

        private static boolean isValid(Album album) {
            return StringUtils.isNotBlank(album.id)
                    && StringUtils.isNotBlank(album.artist)
                    && StringUtils.isNotBlank(album.title)
                    && StringUtils.isNotBlank(album.genre);
        }

        public AlbumBuilder withTitle(String name) {
            subject.title = name;
            return this;
        }

        public AlbumBuilder withArtist(String artist) {
            subject.artist = artist;
            return this;
        }

        public AlbumBuilder withGenre(String genre) {
            subject.genre = genre;
            return this;
        }

        public AlbumBuilder withReleaseDate(Date releaseDate) {
            subject.releaseDate = releaseDate;
            return this;
        }

        public Optional<Album> build() {
            if (isValid(subject)) {
                return Optional.of(subject);
            }
            return Optional.empty();
        }

        public AlbumBuilder withId(String uri) {
            subject.id = uri;
            return this;
        }
    }
}
