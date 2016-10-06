define(
    [
        'js/app',
        'backbone.marionette',
        'js/core/collection/BPMNotificationCollection',
        'js/core/view/BPMNotificationView',
        'text!js/core/template/BPMNotificationCollectionTemplate.html'
    ],

    function (app, Marionette, BPMNotificationCollection, BPMNotificationView, template) {

        'use strict';


        return Marionette.CompositeView.extend({

            template: _.template(template),
            childView: BPMNotificationView,
            childViewContainer: '#notifications',
            initialize: function () {

                var self = this;

                this.collection = new BPMNotificationCollection();
                this.limit = 1;
                app.commands.setHandler('showNotification', function (notification) {
                    self.addNotification(notification);
                })

            },
            addNotification: function (notification) {
                if (this.collection.length == this.limit) {
                    this.collection.pop();
                }
                this.collection.add(notification, {at: 0});
            }

        });

    });