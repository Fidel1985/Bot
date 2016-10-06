define(
    [
        'js/app',
        'js/core/model/BPMNotificationModel'
    ],

    function (app, BPMNotificationModel) {

        'use strict';

        var Notification = function () {

            this.success = function (message) {
                return new BPMNotificationModel({
                    type: app.constants.NOTIFICATION_TYPE.SUCCESS,
                    message: message
                })
            };

            this.info = function (message) {
                return new BPMNotificationModel({
                    type: app.constants.NOTIFICATION_TYPE.INFO,
                    message: message
                })
            };

            this.warning = function (message) {
                return new BPMNotificationModel({
                    type: app.constants.NOTIFICATION_TYPE.WARNING,
                    message: message
                })
            };

            this.danger = function (message) {
                return new BPMNotificationModel({
                    type: app.constants.NOTIFICATION_TYPE.DANGER,
                    message: message
                })
            };

        };

        return new Notification();

    });