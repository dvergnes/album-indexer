package com.github.vergnes.albumindexer.web;

import com.github.vergnes.albumindexer.repository.AlbumRepository;
import com.github.vergnes.albumindexer.web.model.Report;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Created by denis.vergnes on 22/08/2015.
 */
@RestController
public class ReportController {

    @Inject
    private AlbumRepository repository;

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public Report report() {
        Collection<? extends DateHistogram.Bucket> buckets = repository.aggregateAlbumsOverTime();
        return new Report(repository.count(), buckets);
    }
}
