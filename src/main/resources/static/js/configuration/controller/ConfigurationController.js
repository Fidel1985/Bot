define(
    [
        'js/app',
        'backbone.marionette',
        'js/configuration/view/ConfigurationLayoutView',
        'js/configuration/view/UsersTableView',
        'js/configuration/collection/UserAuthorityCollection',
        'js/configuration/collection/UserMetadataCollection'
    ],

    function (app, Marionette, ConfigurationLayoutView, UsersTableView, UserMetadataCollection, UserAuthorityCollection) {
        'use strict';

        return Marionette.Object.extend({

            initialize: function () {
                this.layout = new ConfigurationLayoutView();
            },

            start: function () {
                this.init()
                    //.done(this.layout.prepareView());
                    .done(this.showRegions.bind(this));
            },

            init: function () {
                return $.when(
                    this.initCollection(new UserMetadataCollection(), 'userMetadataCollection'),
                    this.initCollection(new UserAuthorityCollection(), 'userAuthorityCollection'));
            },

            initCollection: function (collection, handlerName) {
                app.reqres.setHandler(handlerName, function () {
                    return collection;
                });
                return collection.fetch();
            },

            showRegions: function () {
                this.layout.showChildView('usersTableRegion', new UsersTableView());
            }
        });

    });