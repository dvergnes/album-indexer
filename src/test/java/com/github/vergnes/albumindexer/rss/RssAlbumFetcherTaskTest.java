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
