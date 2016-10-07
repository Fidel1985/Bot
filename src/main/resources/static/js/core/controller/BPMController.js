define(
    [
        'js/app',
        'js/core/view/BPMLayoutView',
        'js/core/view/BPMHeaderView',
        'js/core/view/BPMNotificationCollectionView',
        'js/core/model/BPMNotification',
        'backbone.marionette'
    ],

    function (app, BPMLayoutView, BPMHeaderView, BPMNotificationCollectionView, BPMNotification) {

        'use strict';

        return Marionette.Object.extend({

            initialize: function () {
                this.layout = new BPMLayoutView();
                this.layout.render();
                this.layout.showChildView('headerRegion', new BPMHeaderView());
                this.layout.showChildView('notificationRegion', new BPMNotificationCollectionView());
            },

            resolveMainPage: function(){

            },

            dashboard: function () {
                var self = this;

                if (app.request('currentUser').hasAuthorityWithName('Project Manager')) {
                    require(['js/dashboard/controller/DashboardController'], function (DashboardController) {
                        var dashboardController;

                        dashboardController = new DashboardController();
                        self.showMainRegion(dashboardController.layout);
                        dashboardController.start();
                    });
                } else {
                    app.execute('showNotification', BPMNotification.danger("Access denied!"));
                }
            },

            configuration: function () {
                var self = this;
                if (app.request('currentUser').hasAuthorityWithName('Admin')) {
                    require(['js/configuration/controller/ConfigurationController'], function (ConfigurationController) {
                        var configurationController;

                        configurationController = new ConfigurationController();
                        self.showMainRegion(configurationController.layout);
                        configurationController.start();
                    });
                } else {
                    app.execute('showNotification', BPMNotification.danger("Access denied!"));
                }
            },

            grid: function () {
                var self = this;
                if (app.request('currentUser').hasAuthorityWithName('Admin')) {
                    require(['js/grid/controller/GridController'], function (GridController) {
                        var gridController;

                        gridController = new GridController();
                        self.showMainRegion(gridController.layout);
                        gridController.start();
                    });
                } else {
                    app.execute('showNotification', BPMNotification.danger("Access denied!"));
                }
            },

            showMainRegion: function (view) {
                this.layout.showChildView('mainRegion', view);
            }

        });

    });