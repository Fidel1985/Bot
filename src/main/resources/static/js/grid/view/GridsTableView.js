define(
    [
        'js/app',
        'js/dashboard/view/JobRowView',
        'text!js/grid/template/GridsTableTemplate.html',
        'backbone.marionette'
    ],

    function (app, JobRowView, template, Marionette) {

        'use strict';

        return Marionette.CompositeView.extend({

            template: _.template(template),
            childView: JobRowView,
            childViewContainer: '[data-rows]',
            initialize: function () {
                this.collection = app.request('jobMetadataCollection');
            }

        });

    });