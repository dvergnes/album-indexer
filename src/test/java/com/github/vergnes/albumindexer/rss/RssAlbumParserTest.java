package com.github.vergnes.albumindexer.rss;

import com.github.vergnes.albumindexer.domain.Album;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RssAlbumParserTest {

    private RssAlbumParser parser = new RssAlbumParser();

    @Test
    public void should_parse_album_with_line_breaks() throws Exception {
        SyndEntry entry = mock(SyndEntry.class);
        SyndContent content = mock(SyndContent.class);
        when(content.getValue()).thenReturn("<p align=\"center\"></p>\n" +
                "<p align=\"center\">\n" +
                "Artist: Nathaniel Rateliff and The Night Sweats\n" +
                "Album: Nathaniel Rateliff and The Night Sweats\n" +
                "Released: 2015\n" +
                "Style: Rock\n" +
                "Format: MP3 320Kbps / FLAC\n" +
                "Size: 87 Mb\n" +
                "\n" +
                "\n" +
                "</p>\n" +
                "<p>Tracklist:\n" +
                "01 &#8211; I Need Never Get Old\n" +
                "02 &#8211; Howling At Nothing\n" +
                "03 &#8211; Trying So Hard Not To Know\n" +
                "04 &#8211; I&#8217;ve Been Failing\n" +
                "05 &#8211; S.O.B.\n" +
                "06 &#8211; Wasting Time\n" +
                "07 &#8211; Thank You\n" +
                "08 &#8211; Look It [...]");
        when(entry.getDescription()).thenReturn(content);

        Album album = parser.parse(entry).get();
        assertThat(album.getArtist()).isEqualTo("Nathaniel Rateliff and The Night Sweats");
        assertThat(album.getTitle()).isEqualTo("Nathaniel Rateliff and The Night Sweats");
        assertThat(album.getGenre()).isEqualTo("Rock");

    }

    @Test
    public void should_not_parse_album_because_invalid_description() throws Exception {
        SyndEntry entry = mock(SyndEntry.class);
        SyndContent content = mock(SyndContent.class);
        when(content.getValue()).thenReturn("<p align=\"center\"></p> good luck to parse this");
        when(entry.getDescription()).thenReturn(content);

        assertThat(parser.parse(entry).isPresent()).isEqualTo(false);
    }
}