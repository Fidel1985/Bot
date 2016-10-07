define(
    [
        'js/app',
        'backbone',
        'backbone.validation'
    ],

    function (app) {

        'use strict';

        return Backbone.Model.extend({
            url: app.constants.URLS.USERS,

            defaults: {
                id: null,
                firstName: '',
                lastName: '',
                username: '',
                password: '',
                email: '',
                authorities: []
            },

            validation: {
                firstName: getValidationRules('First Name'),
                lastName: getValidationRules('Last Name'),
                username: getValidationRules('Username'),
                password: getValidationRules('Password',
                    [{
                        minLength: 5,
                        msg: 'Password length should be equals or greater than 5 characters'
                    }]
                ),
                confirmPassword: getValidationRules('Password Confirmation',
                    [{
                        equalTo: 'password',
                        msg: 'The passwords does not match'
                    }]),
                email: getValidationRules('Email',
                    [{
                        pattern: 'email',
                        msg: 'Please, enter valid Email'
                    }]
                ),
                authorities: function (value) {
                    if (!(value instanceof Array && value.length)) {
                        return 'Please, select at least one role';
                    }
                }
            }
        });

        function getValidationRules(fieldName, additionRules) {
            var defaultValidationRules = getDefaultValidationRules(fieldName);
            return _.union(defaultValidationRules, additionRules ? additionRules : []);
        }

        function getDefaultValidationRules(fieldName) {
            return [{
                required: true,
                msg: 'Please, enter your ' + fieldName
            }, {
                maxLength: 49,
                msg: fieldName + ' length should be equals or less than 49 characters'
            }]
        }
    });
