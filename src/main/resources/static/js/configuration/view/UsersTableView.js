define(
    [
        'js/app',
        'js/configuration/view/UserRowView',
        'text!js/configuration/template/UsersTableTemplate.html',
        'backbone.marionette'
    ],

    function (app, UserRowView, template, Marionette) {

        'use strict';

        return Marionette.CompositeView.extend({

            template: _.template(template),
            childView: UserRowView,
            childViewContainer: '[data-rows]',
            initialize: function () {
                this.collection = app.request('userMetadataCollection');
            }

        });

    });