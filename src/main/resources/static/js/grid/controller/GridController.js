define(
    [
        'js/app',
        'backbone.marionette',
        'js/grid/view/GridLayoutView',
        'js/grid/collection/UserAuthorityCollection'
    ],

    function (app, Marionette, Grid, UserAuthorityCollection) {
        'use strict';

        return Marionette.Object.extend({

            initialize: function () {
                this.layout = new Grid();
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