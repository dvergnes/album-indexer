package com.github.vergnes.albumindexer.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;
import java.util.Optional;

/**
 * Created by denis.vergnes on 21/08/2015.
 */
public class Album {

    private String title;
    private String artist;
    private String genre;
    private double price;
    private String encodingFormat;
    private double encodingRate;
    private Date releaseDate;

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

    }
}
