define(
    [
        'js/app',
        'js/grid/model/UserAuthorityModel',
        'backbone'
    ],

    function (app, UserAuthorityModel) {

        'use strict';

        return Backbone.Collection.extend({
            url: app.constants.URLS.AUTHORITIES,
            model: UserAuthorityModel
        });

    });
