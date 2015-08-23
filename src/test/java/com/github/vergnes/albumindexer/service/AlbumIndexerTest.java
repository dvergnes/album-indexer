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

package com.github.vergnes.albumindexer.service;

import com.github.vergnes.albumindexer.domain.Album;
import com.github.vergnes.albumindexer.repository.AlbumRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AlbumIndexerTest {

    @Mock
    private AlbumRepository repository;

    @InjectMocks
    private AlbumIndexer indexer;

    @Test
    public void should_save_new_album() throws Exception {
        Album album = mock(Album.class);
        String albumId = "newAlbum";
        when(album.getId()).thenReturn(albumId);
        when(repository.exists(albumId)).thenReturn(false);

        indexer.accept(album);

        verify(repository).save(album);
    }

    @Test
    public void should_not_save_existing_album() throws Exception {
        Album album = mock(Album.class);
        String albumId = "oldAlbum";
        when(album.getId()).thenReturn(albumId);
        when(repository.exists(albumId)).thenReturn(true);

        indexer.accept(album);

        verify(repository, never()).save(album);
    }
}