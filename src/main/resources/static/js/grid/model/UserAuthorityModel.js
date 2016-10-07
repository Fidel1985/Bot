define(
    [
        'backbone'
    ],

    function () {

        'use strict';

        return Backbone.Model.extend({
            defaults: {
                id: null,
                name: ''
            }
        })
    });
