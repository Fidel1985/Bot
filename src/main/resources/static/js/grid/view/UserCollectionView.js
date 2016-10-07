define(
    [
        'js/app',
        'backbone.marionette',
        'text!js/grid/template/UserCollectionTemplate.html',
        'backbone.stickit'
    ],

    function (app, Marionette, template) {

        'use strict';

        return Marionette.ItemView.extend({

            template: _.template(template)

        });

    });