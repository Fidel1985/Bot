define(
    [
        'text!js/grid/template/GridConfigurationTemplate.html',
        'js/app',
        'js/core/model/BPMNotification',
        'backbone.marionette',
        'select2'
    ], function (template, app, BPMNotification) {

        'use strict';

        return Marionette.ItemView.extend({
            template: _.template(template),
            onRender: registerComponents,
            ui: {
                submitBtn: '#submitBtn',
                cancelBtn: '#cancelBtn',
                password: '#password',
                confirmPassword: '#confirmPassword',
                authorities: '#authorities'
            },

            events: {
                'click @ui.submitBtn': saveUser,
                'click @ui.cancelBtn': hideForm
            },

            bindings: {
                '#firstName': registerObserve('firstName'),
                '#lastName': registerObserve('lastName'),
                '#username': registerObserve('username'),
                '#email': registerObserve('email'),
                '#password': registerObserve('password'),
                '#confirmPassword': registerObserve('confirmPassword'),
                '#authorities': {
                    observe: 'authorities',
                    selectOptions: {
                        collection: function () {
                            return app.request('userAuthorityCollection');
                        },
                        labelPath: 'name',
                        defaultOption: {
                            label: '',
                            value: null
                        }
                    }
                }
            }
        });

        function registerComponents() {
            this.model.set('confirmPassword', '');
            registerValidator(this);
            this.stickit();
            this.ui.authorities.select2();
        }

        function registerValidator(instance) {
            Backbone.Validation.bind(instance, {
                valid: function (view, attr) {
                    var div = getParentDiv(attr, view);
                    div.addClass('has-success').removeClass('has-error');
                    div.find('.glyphicon').removeClass('glyphicon-pencil glyphicon-remove').addClass('glyphicon-ok');
                    div.find('.help-block').html('').addClass('hidden');
                },
                invalid: function (view, attr, error) {
                    var div = getParentDiv(attr, view);
                    div.addClass('has-error').removeClass('has-success');
                    div.find('.glyphicon').removeClass('glyphicon-pencil glyphicon-ok').addClass('glyphicon-remove');
                    div.find('.help-block').html(error).removeClass('hidden');
                }
            });
        }

        function registerObserve(name) {
            return {
                observe: name,
                onSet: function (value) {
                    return $.trim(value);
                }
            }
        }

        function saveUser() {
            var self = this;
            if (this.model.isValid(true)) {
                this.model.save({}, {
                    success: function () {
                        app.execute('showNotification', BPMNotification.success('User created successfully.'));
                        self.triggerMethod('user:created');
                    },
                    error: function (model, jqXHR) {
                        app.execute('showNotification', BPMNotification.danger(jqXHR.responseText));
                        refreshUI(self);
                    },
                    wait: true,
                    validate: false
                });
            }
        }

        function hideForm() {
            this.destroy();
        }

        function getParentDiv(attr, view) {
            var currentElement, div;
            currentElement = view.$('#' + attr);
            div = currentElement.closest('.form-group');
            return div;
        }

        function refreshUI(instance) {
            instance.$('.has-feedback').removeClass('has-success');
            instance.$('.glyphicon').toggleClass('glyphicon-ok glyphicon-pencil');
        }
    });

