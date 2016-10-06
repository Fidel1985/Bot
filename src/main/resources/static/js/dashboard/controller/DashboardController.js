define(
    [
        'js/app',
        'backbone.marionette',
        'js/dashboard/collection/JobMetadataCollection',
        'js/dashboard/collection/LanguageCollection',
        'js/dashboard/view/DashboardLayoutView',
        'js/dashboard/view/JobCreateView',
        'js/dashboard/view/JobsTableView',
        'js/dashboard/controller/JobStatusFlow'
    ],

    function (app, Marionette, JobMetadataCollection, LanguageCollection, DashboardLayoutView, JobCreateView, JobsTableView, JobsStatusFlow) {

        'use strict';

        return Marionette.Object.extend({

            initialize: function () {
                this.layout = new DashboardLayoutView();
            },

            start: function () {
                this.init()
                    .done(this.showRegions.bind(this)
                );
                //TODO: handle errors
            },

            init: function () {
                return $.when(
                    this.initCollection(new JobMetadataCollection(), 'jobMetadataCollection'),
                    this.initCollection(new LanguageCollection(), 'languageCollection'),
                    this.initJobStatusFlow());

            },

            initJobStatusFlow: function () {
                var jobStatusFlow;

                jobStatusFlow = new JobsStatusFlow();
                app.reqres.setHandler('jobStatusFlow', function () {
                    return jobStatusFlow;
                });
                return $.Deferred().resolve().promise();

            },

            initCollection: function (collection, handlerName) {
                app.reqres.setHandler(handlerName, function () {
                    return collection;
                });
                return collection.fetch();
            },

            showRegions: function () {
                this.layout.showChildView('jobsTableRegion', new JobsTableView());
                this.layout.showChildView('jobCreateRegion', new JobCreateView());
            }

        });

    });