define(
    [
        'js/app',
        'backbone.marionette',
        'text!js/core/template/BPMHeaderViewTemplate.html',
        'backbone.stickit'
    ],

    function (app, Marionette, template) {

        'use strict';

        return Marionette.ItemView.extend({

            template: _.template(template),
            ui: {
                removeFileBtn: '[data-remove-file]'
            },
            bindings: {
                '#dashboard': {
                    observe: 'authorities',
                    visible: isProjectManager
                },
                '#configuration': {
                    observe: 'authorities',
                    visible: isAdmin
                },
                '#grid': {
                    observe: 'authorities',
                    visible: isAdmin
                },
                '#username': 'username'
            },
            initialize: function () {
                this.model = app.request('currentUser');
            },
            onRender: function () {
                this.stickit();
            }

        });

        function isProjectManager(authorities) {
            return findWithName(authorities, 'Project Manager');
        }

        function isAdmin(authorities) {
            return findWithName(authorities, 'Admin');
        }

        function findWithName(authorities, name) {
            return _.find(authorities, function (authority) {
                return authority.name === name;
            });
        }

    });