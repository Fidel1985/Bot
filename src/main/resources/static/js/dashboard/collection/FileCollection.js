define(
    [
        'js/app',
        'backbone',
        'js/dashboard/model/FileModel'
    ],

    function (app, Backbone, FileModel) {

        'use strict';

        return Backbone.Collection.extend({
               model: FileModel
        });

    });