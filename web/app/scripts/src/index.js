var d3 = require('d3');
var moment = require('moment');

/**
 * Created by denis.vergnes on 23/08/2015.
 */
(function (global) {
    'use strict';

    var config = {
        dataSource: 'http://localhost:8080/api/report',
        colors: ['#3366CC', '#DC3912', '#FF9900', '#109618', '#990099', '#FF3300', '#CC6633', '#1293CD', '#0099FF', '#186901'],
        dateFormat: 'YYYY-MM-DD'
    };
    var log = global.console;

    log.log('Starting application');

    function TotalCountController(el) {

        function _update(data) {
            el.innerHTML = data;
        }

        return {update: _update};
    }

    var totalCountController = new TotalCountController(global.document.querySelector('.total-count'));

    function formatPercent(value, total) {
        return (value * 100 / total).toFixed() + '%'
    }


    function GenreController(el) {

        var width = 960,
            height = 500,
            radius = Math.min(width, height) / 2;

        var color = d3.scale.ordinal()
            .range(config.colors);

        var arc = d3.svg.arc()
            .outerRadius(radius - 10)
            .innerRadius(0);

        var pie = d3.layout.pie()
            .sort(null)
            .value(function (d) {
                return d.count;
            });

        var svg = d3.select(el).append('svg')
            .attr('width', width)
            .attr('height', height)
            .append('g')
            .attr('transform', 'translate(' + width / 2 + ',' + height / 2 + ')');

        function _update(report) {
            var data = report.albumsByGenre;
            data.forEach(function (d) {
                d.count = +d.count;
            });

            var g = svg.selectAll('.arc')
                .data(pie(data))
                .enter().append('g')
                .attr('class', 'arc');

            g.append('path')
                .attr('d', arc)
                .style('fill', function (d) {
                    return color(d.data.key);
                });

            g.append('text')
                .attr('transform', function (d) {
                    return 'translate(' + arc.centroid(d) + ')';
                })
                .attr('dy', '.35em')
                .style('text-anchor', 'middle')
                .text(function (d) {
                    return d.data.key + ' ' + formatPercent(d.data.count, report.totalCount);
                });
        }

        return {update: _update};
    }

    var genreController = new GenreController(global.document.querySelector('.albums-genre'));


    function HistogramController(el) {


        var margin = {top: 20, right: 20, bottom: 30, left: 40},
            width = 960 - margin.left - margin.right,
            height = 500 - margin.top - margin.bottom;

        var x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1);

        var y = d3.scale.linear()
            .range([height, 0]);

        var xAxis = d3.svg.axis()
            .scale(x)
            .orient('bottom');

        var yAxis = d3.svg.axis()
            .scale(y)
            .orient('left');

        var svg = d3.select(el).append('svg')
            .attr('width', width + margin.left + margin.right)
            .attr('height', height + margin.top + margin.bottom)
            .append('g')
            .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

        function _update(data) {
            data.forEach(function (d) {
                d.formattedDate = moment(d.key).format(config.dateFormat)
            });
            x.domain(data.map(function (d) {
                return d.formattedDate;
            }));
            y.domain([0, d3.max(data, function (d) {
                return d.count;
            })]);

            svg.append('g')
                .attr('class', 'x axis')
                .attr('transform', 'translate(0,' + height + ')')
                .call(xAxis);

            svg.append('g')
                .attr('class', 'y axis')
                .call(yAxis)
                .append('text')
                .attr('transform', 'rotate(-90)')
                .attr('y', 6)
                .attr('dy', '.71em')
                .style('text-anchor', 'end')
                .text('Count');

            svg.selectAll('.bar')
                .data(data)
                .enter().append('rect')
                .attr('class', 'bar')
                .attr('x', function (d) {
                    return x(d.formattedDate);
                })
                .attr('width', x.rangeBand())
                .attr('y', function (d) {
                    return y(d.count);
                })
                .attr('height', function (d) {
                    return height - y(d.count);
                });
        }

        return {update: _update};

    }

    var histogramController = new HistogramController(global.document.querySelector('.albums-overtime'));


    function fetchAndUpdate() {
        d3.json(config.dataSource, function (error, report) {
            if (error) {
                log.error('Unable to fetch data', error);
            } else {
                log.debug('fetching report from data source', report);
                totalCountController.update(report.totalCount);
                genreController.update(report);
                histogramController.update(report.albumsOverTime);
            }
        });
    }

    fetchAndUpdate();

})(window);