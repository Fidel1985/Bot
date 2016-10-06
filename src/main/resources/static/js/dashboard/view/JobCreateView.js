define(
    [
        'js/app',
        'backbone.marionette',
        'text!js/dashboard/template/JobCreateTemplate.html',
        'js/dashboard/model/JobMetadataModel',
        'js/dashboard/view/JobFormView'
    ],

    function (app, Marionette, template, JobMetadataModel, JobFormView) {

        'use strict';

        return Marionette.LayoutView.extend({

            template: _.template(template),

            regions: {
                jobFormRegion: '[data-job-from-region]'
            },

            ui: {
                'createJobBtn': '#createJobBtn'
            },

            events: {
                'click @ui.createJobBtn': 'showJobForm'
            },

            childEvents: {
                'jobForm:close': 'closeJobForm',
                'job:created': 'onJobCreated'
            },

            initialize: function () {
                this.collection = app.request('jobMetadataCollection');
            },

            showJobForm: function () {

                var jobModel, jobFormView;

                jobModel = new JobMetadataModel();
                jobFormView = new JobFormView({model: jobModel});
                this.showChildView('jobFormRegion', jobFormView);
                this.ui.createJobBtn.hide();

            },

            onJobCreated: function (view) {
                this.collection.add(view.model);
                this.closeJobForm();
            },

            closeJobForm: function () {
                this.getRegion('jobFormRegion').empty();
                this.ui.createJobBtn.show();
            }
        });
    });