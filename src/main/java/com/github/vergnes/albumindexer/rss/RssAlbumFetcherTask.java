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
import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by denis.vergnes on 21/08/2015.
 */
@Component
public class RssAlbumFetcherTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Inject
    private FeedFetcher feedFetcher;
    @Inject
    private RssAlbumParser rssAlbumParser;
    @Inject
    private URL url;
    @Inject
    private Consumer<? super Album> consumer;

    @Scheduled(fixedRateString = "${rss.update.frequencyinms}")
    public void fetchAlbums() {
        LOGGER.info("Fetching rss feed for new albums");
        try {
            SyndFeed syndFeed = feedFetcher.retrieveFeed(url);
            LOGGER.debug("Feed last update: {} contains {} entries", syndFeed.getPublishedDate(), 
                    syndFeed.getEntries().size());
            syndFeed.getEntries().stream()
                    .map(rssAlbumParser::parse)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(consumer);
        } catch (IOException | FeedException | FetcherException e) {
            LOGGER.error("Unable to fetch albums from RSS feed", e);
        }

    }
}
