define(
    [
        'js/app',
        'backbone'
    ],

    function (app, Backbone) {

        'use strict';

        return Backbone.Model.extend({
            url: app.constants.URLS.JOBS,

            defaults: function () {
                return {
                    tag: '',
                    displayName: ''
                };
            }
        });

    });

