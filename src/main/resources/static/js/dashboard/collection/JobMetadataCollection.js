define(
    [
        'js/app',
        'js/dashboard/model/JobMetadataModel',
        'backbone'
    ],

    function (app, JobMetadataModel) {

        'use strict';

        return Backbone.Collection.extend({
            url: app.constants.URLS.JOBS,
            model: JobMetadataModel
        });

    });