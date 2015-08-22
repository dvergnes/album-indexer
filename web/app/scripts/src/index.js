var d3 = require('d3');

/**
 * Created by denis.vergnes on 23/08/2015.
 */
(function (global) {
    'use strict';

    global.console.log('Starting application');

    function TotalCountController(el) {
        var totalCountEl = el;

        function _update(data) {
            el.innerHTML = data;
        }

        return {update: _update};
    }

    var totalCountController = new TotalCountController(global.document.querySelector('.total-count'));

    totalCountController.update(5);

})(window);