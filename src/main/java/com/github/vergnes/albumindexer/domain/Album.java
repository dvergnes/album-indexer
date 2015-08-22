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
    private double price;
    private String encodingFormat;
    private double encodingRate;
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

    public double getPrice() {
        return price;
    }

    public String getEncodingFormat() {
        return encodingFormat;
    }

    public double getEncodingRate() {
        return encodingRate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("artist", artist)
                .append("genre", genre)
                .append("price", price)
                .append("encodingFormat", encodingFormat)
                .append("encodingRate", encodingRate)
                .append("releaseDate", releaseDate)
                .toString();
    }

    public static class AlbumBuilder {
        private Album subject = new Album();

        private static boolean isValid(Album album) {
            return StringUtils.isNotBlank(album.artist)
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

        public AlbumBuilder withPrice(double price) {
            subject.price = price;
            return this;
        }

        public AlbumBuilder withEncodingFormat(String encodingFormat) {
            subject.encodingFormat = encodingFormat;
            return this;
        }

        public AlbumBuilder withEncodingRate(double encodingRate) {
            subject.encodingRate = encodingRate;
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
