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

package com.github.vergnes.albumindexer.repository;

import com.github.vergnes.albumindexer.AlbumIndexerApplication;
import com.github.vergnes.albumindexer.domain.Album;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlbumIndexerApplication.class)
public class AlbumRepositoryImplIT {
    
    /*@Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

	@Before
	public void before() {
		elasticsearchTemplate.deleteIndex(ArticleEntity.class);
		elasticsearchTemplate.createIndex(ArticleEntity.class);
		elasticsearchTemplate.putMapping(ArticleEntity.class);
		elasticsearchTemplate.refresh(ArticleEntity.class, true);

		IndexQuery article1 = new ArticleEntityBuilder("1").title("article four").subject("computing").addAuthor
		(RIZWAN_IDREES).addAuthor(ARTUR_KONCZAK).addAuthor(MOHSIN_HUSEN).addAuthor(JONATHAN_YAN).score(10)
		.buildIndex();
		IndexQuery article2 = new ArticleEntityBuilder("2").title("article three").subject("computing").addAuthor
		(RIZWAN_IDREES).addAuthor(ARTUR_KONCZAK).addAuthor(MOHSIN_HUSEN).addPublishedYear(YEAR_2000).score(20)
		.buildIndex();
		IndexQuery article3 = new ArticleEntityBuilder("3").title("article two").subject("computing").addAuthor
		(RIZWAN_IDREES).addAuthor(ARTUR_KONCZAK).addPublishedYear(YEAR_2001).addPublishedYear(YEAR_2000).score(30)
		.buildIndex();
		IndexQuery article4 = new ArticleEntityBuilder("4").title("article one").subject("accounting").addAuthor
		(RIZWAN_IDREES).addPublishedYear(YEAR_2002).addPublishedYear(YEAR_2001).addPublishedYear(YEAR_2000).score(40)
		.buildIndex();

		elasticsearchTemplate.index(article1);
		elasticsearchTemplate.index(article2);
		elasticsearchTemplate.index(article3);
		elasticsearchTemplate.index(article4);
		elasticsearchTemplate.refresh(ArticleEntity.class, true);
	}

	@Test
	public void shouldReturnAggregatedResponseForGivenSearchQuery() {
		// given
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchAllQuery())
				.withSearchType(COUNT)
				.withIndices("articles").withTypes("article")
				.addAggregation(terms("subjects").field("subject"))
				.build();
		// when
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});
		// then
		assertThat(aggregations, is(notNullValue()));
		assertThat(aggregations.asMap().get("subjects"), is(notNullValue()));
	}
	*/

    @Inject
    private ElasticsearchTemplate template;

    @Inject
    private AlbumRepository repository;

    @Before
    public void setUp() throws Exception {
        template.deleteIndex(Album.class);
        template.createIndex(Album.class);
        template.putMapping(Album.class);
        template.refresh(Album.class, true);


        Album nevermind = new Album.AlbumBuilder()
                .withArtist("Nirvane")
                .withTitle("Nevermind")
                .withGenre("Grunge")
                .withId("1234")
                .withReleaseDate(DateTime.parse("1991-09-24").toDate())
                .build().get();
        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(nevermind.getId());
        indexQuery1.setObject(nevermind);
        template.index(indexQuery1);

        Album faceTheNation = new Album.AlbumBuilder()
                .withArtist("Kid 'n Play")
                .withTitle("Face The Nation")
                .withGenre("Hip Hop")
                .withId("5678")
                .withReleaseDate(DateTime.parse("1991-09-24").toDate())
                .build().get();
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(faceTheNation.getId());
        indexQuery2.setObject(faceTheNation);
        template.index(indexQuery2);

        Album thingFallApart = new Album.AlbumBuilder()
                .withArtist("The Roots")
                .withTitle("Things Fall Apart")
                .withGenre("Hip Hop")
                .withId("8901")
                .withReleaseDate(DateTime.parse("1999-02-23").toDate())
                .build().get();
        IndexQuery indexQuery3 = new IndexQuery();
        indexQuery3.setId(thingFallApart.getId());
        indexQuery3.setObject(thingFallApart);
        template.index(indexQuery3);

        template.refresh(Album.class, true);

    }

    @Test
    public void should_AggregateAlbumsOverTime() throws Exception {
        Collection<? extends DateHistogram.Bucket> buckets = repository.aggregateAlbumsOverTime();
        assertThat(buckets).hasSize(2);
    }

    @Test
    public void should_AggregateAlbumsByGenre() throws Exception {
        Collection<? extends MultiBucketsAggregation.Bucket> buckets = repository.aggregateAlbumsByGenre();
        assertThat(buckets).hasSize(2);
    }
}