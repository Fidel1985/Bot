define(
    [
        'js/app',
        'backbone.marionette',
        'text!js/dashboard/template/JobRowTemplate.html',
        'js/dashboard/view/JobStatusView',
        'js/dashboard/view/JobDetailView',
        'backbone.stickit',
        'jquery.file.upload'
    ],

    function (app, Marionette, template, JobStatusView, JobDetailView) {

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

            events: {
                'click @ui.showDetailBtn': showJobDetailView
            },

            bindings: {
                '[data-id]': 'id',
                '[data-name]': 'name',
                '[data-description]': 'description'
            },

            onRender: function () {
                this.stickit();
            },

            onBeforeShow: function () {
                this.showChildView('jobStatus', new JobStatusView({model: this.model}));
            }

        });

        function showJobDetailView() {

            var jobDetailRegion;

            jobDetailRegion = this.getRegion('jobDetail');
            if (jobDetailRegion.hasView()) {
                this.ui.showDetailBtn.removeClass('active');
                jobDetailRegion.empty();
            } else {
                this.ui.showDetailBtn.addClass('active');
                jobDetailRegion.show(new JobDetailView({model: this.model}));
            }

        }

    });