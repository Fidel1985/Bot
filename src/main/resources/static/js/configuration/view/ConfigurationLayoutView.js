define(
    [
        'text!js/configuration/template/ConfigurationLayoutTemplate.html',
        'js/app',
        'js/core/model/BPMNotification',
        'js/configuration/view/UserFormView',
        'js/configuration/model/UserModel',
        'backbone.marionette'
    ],

    function (template, app, BPMNotification, UserFormView, UserModel) {
        'use strict';

        return Marionette.LayoutView.extend({

            template: _.template(template),

            regions: {
                userConfigurationRegion: '[data-user-configuration-region]',
                userCollectionRegion: '[data-user-collection-region]',
                usersTableRegion: '[data-user-table-region]',
            },
            ui: {
                'showUserFormBtn': '#showUserFormBtn'
            },
            events: {
                'click @ui.showUserFormBtn': showUserFormView
            },
            childEvents: {
                'user:created': onUserCreated,
                'destroy': showUserCreateBtn
            },
            prepareView: showUserCreateBtn
        });

        function onUserCreated() {
            this.ui.showUserFormBtn.show();
            this.getRegion('userConfigurationRegion').empty();
        }

        function showUserCreateBtn() {
            this.ui.showUserFormBtn.show();
        }

        function showUserFormView() {
            var userFormView = new UserFormView({
                model: new UserModel()
            });
            this.showChildView('userConfigurationRegion', userFormView);
            this.ui.showUserFormBtn.hide();
        }
    });