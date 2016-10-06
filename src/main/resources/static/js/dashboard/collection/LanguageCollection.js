define(
    [
        'js/app',
        'backbone',
        'js/dashboard/model/LanguageModel'
    ],

    function (app, Backbone, LanguageModel) {

        'use strict';

        return Backbone.Collection.extend({
            url: app.constants.URLS.LANGUAGES,
            model: LanguageModel
        });

    });