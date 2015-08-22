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
