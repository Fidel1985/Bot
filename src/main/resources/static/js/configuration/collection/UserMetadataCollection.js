define(
    [
        'js/app',
        'js/configuration/model/UserModel',
        'backbone'
    ],

    function (app, UserModel) {

        'use strict';

        return Backbone.Collection.extend({
            url: app.constants.URLS.USERS,
            model: UserModel
        });

    });