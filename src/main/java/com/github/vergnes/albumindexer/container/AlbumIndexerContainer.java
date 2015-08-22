package com.github.vergnes.albumindexer.container;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.HashMapFeedInfoCache;
import com.rometools.fetcher.impl.HttpClientFeedFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by denis.vergnes on 22/08/2015.
 */
@Configuration
public class AlbumIndexerContainer {

    @Value("${rss.url}")
    private URL feedUrl;

    @Bean
    public FeedFetcher feedFetcher() {
        FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
        return new HttpClientFeedFetcher(feedInfoCache);
    }

    @Bean
    public URL rssFeedUrl() throws MalformedURLException {
        return feedUrl;
    }
}
