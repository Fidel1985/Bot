define(
    [
        'text!js/core/template/BPMLayoutTemplate.html',
        'backbone.marionette'
    ],

    function (template) {

        'use strict';

        return Marionette.LayoutView.extend({

            el : '[data-view]',
            template : _.template(template),
            regions: {
                headerRegion: '[data-header-region]',
                notificationRegion: '[data-notification-region]',
                mainRegion: '[data-main-region]',
                footerRegion: '[data-footer-region]'
            }

        });

    });
