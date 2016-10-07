define(
    [
        'backbone.marionette'
    ],

    function () {

        'use strict';

        return Marionette.AppRouter.extend({

            appRoutes: {
                '': 'resolveMainPage',
                'dashboard': 'dashboard',
                'configuration': 'configuration',
                'grid': 'grid'
            }
        });

    });