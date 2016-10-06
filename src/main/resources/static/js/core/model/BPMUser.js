define(
    [
        'js/app',
        'backbone',
        'backbone.validation',
    ],

    function (app) {

        'use strict';

        return Backbone.Model.extend({
            url: 'session/user',

            defaults: {
                id: null,
                firstName: '',
                lastName: '',
                username: '',
                email: '',
                authorities: []
            },

            hasAuthorityWithName: function(name){
                var authorities;

                authorities = this.get('authorities');
                return authorities && findWithName(authorities, name);
            }
        });

        function findWithName(authorities, name) {
            return _.find(authorities, function (authority) {
                return authority.name === name;
            });
        }

    });
