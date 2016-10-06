define(
    [
        'js/app',
        'backbone',
        'js/core/model/BPMNotificationModel'
    ],

    function (app, Backbone, BPMNotificationModel) {

        'use strict';

        return Backbone.Collection.extend({
            model: BPMNotificationModel
        });

    });