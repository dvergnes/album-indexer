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

package com.github.vergnes.albumindexer.rss;

import com.github.vergnes.albumindexer.domain.Album;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by denis.vergnes on 21/08/2015.
 */
@Component
public class RssAlbumParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static Pattern DESCRIPTION_PATTERN = Pattern.compile(
            "</p>.*<p align=\"center\">(.*)</p>", Pattern.MULTILINE | Pattern.DOTALL);

    public Optional<Album> parse(SyndEntry entry) {
        Album.AlbumBuilder builder = extractAlbumInfo(entry.getDescription());
        builder.withId(entry.getUri());
        builder.withReleaseDate(entry.getPublishedDate());
        return builder.build();
    }

    private Album.AlbumBuilder extractAlbumInfo(SyndContent description) {
        Album.AlbumBuilder builder = new Album.AlbumBuilder();
        String descriptionValue = description.getValue();
        Matcher matcher = DESCRIPTION_PATTERN.matcher(descriptionValue);
        Map<String, String> values = new HashMap<>();
        while (matcher.find()) {
            String currentMatch = matcher.group(1);
            LOGGER.debug("Extract values from: {}", currentMatch);
            values.putAll(parseAlbumInfos(currentMatch));
        }
        builder.withArtist(values.get("artist"));
        builder.withTitle(values.get("album"));
        builder.withGenre(values.get("style"));
        return builder;
    }

    private Map<String, String> parseAlbumInfos(String currentMatch) {
        try (BufferedReader reader = new BufferedReader(new StringReader(currentMatch))) {
            String line = reader.readLine();
            Map<String, String> values = new HashMap<>();
            while (line != null) {
                String[] tokens = line.split(":");
                if (tokens.length == 2) {
                    values.put(tokens[0].toLowerCase(), tokens[1].trim());
                } else {
                    LOGGER.debug("Skip line: {} because unable to extract a key/value pair", line);
                }
                line = reader.readLine();
            }
            return values;
        } catch (IOException e) {
            // really unlikely to happen because everything is in memory
            LOGGER.error("Unable to parse data from: {}", e);
        }
        return Collections.emptyMap();
    }
}
