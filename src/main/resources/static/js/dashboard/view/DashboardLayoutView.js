define(
    [
        'backbone.marionette',
        'text!js/dashboard/template/DashboardLayoutTemplate.html'

    ],

    function (Marionette, template) {

        'use strict';

        return Marionette.LayoutView.extend({

            template: _.template(template),

            regions: {
                jobsTableRegion: '[data-job-table-region]',
                jobCreateRegion: '[data-job-create-region]'
            }

        });

    });
