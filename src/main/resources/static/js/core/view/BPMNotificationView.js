define(
    [
        'js/app',
        'backbone.marionette',
        'text!js/core/template/BPMNotificationTemplate.html',
        'backbone.stickit'
    ],

    function (app, Marionette, template) {

        'use strict';

        var notificationTypeClassMap, NotificationType;

        NotificationType = app.constants.NOTIFICATION_TYPE;
        notificationTypeClassMap = {};
        notificationTypeClassMap[NotificationType.SUCCESS] = 'alert alert-dismissible alert-success';
        notificationTypeClassMap[NotificationType.INFO] = 'alert alert-dismissible alert-info';
        notificationTypeClassMap[NotificationType.WARNING] = 'alert alert-dismissible alert-warning';
        notificationTypeClassMap[NotificationType.DANGER] = 'alert alert-dismissible alert-danger';

        return Marionette.ItemView.extend({

            template: _.template(template),
            ui: {
                message: '[data-notification-message]',
                closeBtn: '.close'
            },
            bindings: {
                '[data-notification-type]': {
                    attributes: [{
                        name: 'class',
                        observe: 'type',
                        onGet: function (type) {
                            return notificationTypeClassMap[type];
                        }
                    }]
                },
                '[data-notification-message]': 'message'

            },
            events: {
                "click @ui.closeBtn": 'close'
            },
            onRender: function () {
                this.stickit();
            },
            close: function () {
                this.model.collection.remove(this.model);
            }

        });

    });