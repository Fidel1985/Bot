define(
    [
        'js/app',
        'backbone.marionette',
        'js/configuration/collection/UserMetadataCollection',
        'js/configuration/view/ConfigurationLayoutView',
        'js/configuration/view/UsersTableView',
        'js/configuration/collection/UserAuthorityCollection'
    ],

    function (app, Marionette, ConfigurationLayoutView, UsersTableView, UserMetadataCollection, UserAuthorityCollection) {
        'use strict';

        return Marionette.Object.extend({

            initialize: function () {
                this.layout = new ConfigurationLayoutView();
            },

            start: function () {
                $.when(
                        this.initCollection(new UserMetadataCollection(), 'userMetadataCollection'),
                        this.initCollection(new UserAuthorityCollection(), 'userAuthorityCollection'))
                    .done(this.layout.prepareView());
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