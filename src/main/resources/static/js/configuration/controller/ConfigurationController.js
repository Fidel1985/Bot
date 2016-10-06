define(
    [
        'js/app',
        'backbone.marionette',
        'js/configuration/view/ConfigurationLayoutView',
        'js/configuration/collection/UserAuthorityCollection'
    ],

    function (app, Marionette, ConfigurationLayoutView, UserAuthorityCollection) {
        'use strict';

        return Marionette.Object.extend({

            initialize: function () {
                this.layout = new ConfigurationLayoutView();
            },

            start: function () {
                $.when(this.initCollection(new UserAuthorityCollection(), 'userAuthorityCollection'))
                    .done(this.layout.prepareView());
            },

            initCollection: function (collection, handlerName) {
                app.reqres.setHandler(handlerName, function () {
                    return collection;
                });
                return collection.fetch();
            }
        });

    });