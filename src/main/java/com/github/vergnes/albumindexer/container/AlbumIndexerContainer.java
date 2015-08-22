package com.github.vergnes.albumindexer.container;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.HashMapFeedInfoCache;
import com.rometools.fetcher.impl.HttpClientFeedFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by denis.vergnes on 22/08/2015.
 */
@Configuration
public class AlbumIndexerContainer {

    @Bean
    public FeedFetcher feedFetcher() {
        FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
        return new HttpClientFeedFetcher(feedInfoCache);
    }

    @Bean
    public URL rssFeedUrl() throws MalformedURLException {
        // FIXME: should be in configuration
        return new URL("http://newalbumreleases.net/feed/");
    }
}