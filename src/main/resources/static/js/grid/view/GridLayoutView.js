define(
    [
        'text!js/grid/template/GridLayoutTemplate.html',
        'js/app',
        'js/core/model/BPMNotification',
        'js/grid/view/UserFormView',
        'js/grid/model/UserModel',
        'backbone.marionette'
    ],

    function (template, app, BPMNotification, UserFormView, UserModel) {
        'use strict';

        return Marionette.LayoutView.extend({

            template: _.template(template),

            regions: {
                userGridRegion: '[data-user-configuration-region]',
                userCollectionRegion: '[data-user-collection-region]'
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
            this.getRegion('userGridRegion').empty();
        }

        function showUserCreateBtn() {
            this.ui.showUserFormBtn.show();
        }

        function showUserFormView() {
            var userFormView = new UserFormView({
                model: new UserModel()
            });
            this.showChildView('userGridRegion', userFormView);
            this.ui.showUserFormBtn.hide();
        }
    });