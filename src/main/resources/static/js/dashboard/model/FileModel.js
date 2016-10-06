define(
    [
        'js/app',
        'backbone'
    ],

    function (app, Backbone) {

        'use strict';

        return Backbone.Model.extend({

            defaults: function () {
                return {
                    id: null,
                    name: '',
                    size: null
                };
            }
        });

    });
