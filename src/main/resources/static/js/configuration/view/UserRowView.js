define(
    [
        'js/app',
        'backbone.marionette',
        'text!js/configuration/template/UserRowTemplate.html',
        /*'js/dashboard/view/JobStatusView',
        'js/dashboard/view/JobDetailView',*/
        'backbone.stickit',
        'jquery.file.upload'
    ],

    function (app, Marionette, template) {

        'use strict';

        return Marionette.LayoutView.extend({

           template: _.template(template),

            regions: {
                jobStatus: '[data-status]',
                jobDetail: '[data-job-detail]'
            },

            ui: {
                'jobRow': '.row',
                'uploadFile': '.uploadFile',
                'showDetailBtn': '[data-show-detail]'
            },

            bindings: {
                '[data-id]': 'id',
                '[data-name]': 'name',
                '[data-description]': 'description'
            },


            onRender: function () {
                this.stickit();
            }

        });

    });