package com.github.vergnes.albumindexer.rss;

import com.github.vergnes.albumindexer.domain.Album;
import com.rometools.fetcher.FeedFetcher;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by denis.vergnes on 21/08/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class RssAlbumFetcherTaskTest {

    @Mock
    private RssAlbumParser parser;

    @Mock
    private FeedFetcher fetcher;

    @Mock
    private Consumer<Album> consumer;

    @InjectMocks
    private RssAlbumFetcherTask task;

    @Test
    public void should_retrieve_albums() throws Exception {
        SyndFeed syndFeed = mock(SyndFeed.class);
        SyndEntry entry1 = mock(SyndEntry.class);
        SyndEntry entry2 = mock(SyndEntry.class);
        SyndEntry entry3 = mock(SyndEntry.class);
        when(syndFeed.getEntries()).thenReturn(Arrays.asList(entry1, entry2, entry3));

        when(fetcher.retrieveFeed(null)).thenReturn(syndFeed);

        Album album1 = mock(Album.class);
        Album album2 = mock(Album.class);
        when(parser.parse(any(SyndEntry.class))).thenReturn(Optional.of(album1), Optional.empty(), Optional.of(album2));

        task.fetchAlbums();

        verify(consumer).accept(album1);
        verify(consumer).accept(album2);
    }

}
