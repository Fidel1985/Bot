define(
    [
        'js/app',
        'js/dashboard/view/DropZoneView',
        'js/dashboard/view/SourceFileCollectionView',
        'js/core/model/BPMNotification',
        'text!js/dashboard/template/JobFormTemplate.html',
        'backbone.marionette',
        'backbone.stickit',
        'select2',
        'jquery.file.upload'
    ],

    function (app, DropZoneView, SourceFileCollectionView, BPMNotification, template) {

        'use strict';

        return Marionette.LayoutView.extend({

            template: _.template(template),
            regions: {
                dropZone: '#dropZone',
                fileCollection: '#fileCollection'
            },
            ui: {
                form: 'form',
                createBtn: '#createBtn',
                cancelBtn: '#cancelBtn',
                srcLngSelect: '#sourceLanguage',
                tgtLngsSelect: '#targetLanguages'
            },
            events: {
                'click @ui.createBtn': saveJob,
                'click @ui.cancelBtn': close
            },
            bindings: {
                '#name': 'name',
                '#description': 'description',
                '#sourceLanguage': {
                    observe: 'sourceLanguage',
                    selectOptions: {
                        collection: function () {
                            return app.request('languageCollection');
                        },
                        labelPath: 'tag',
                        defaultOption: {
                            label: '',
                            value: null
                        }
                    }
                },
                '#targetLanguages': {
                    observe: 'targetLanguages',
                    selectOptions: {
                        collection: function () {
                            return app.request('languageCollection');
                        },
                        labelPath: 'tag'
                    }
                }
            },
            onRender: registerComponents,
            onBeforeShow: function () {
                this.showChildView('dropZone', new DropZoneView({collection: this.model.get('sourceFiles')}));
                this.showChildView('fileCollection', new SourceFileCollectionView({job: this.model}));
            }
        });

        function registerComponents() {
            this.stickit();
            this.ui.srcLngSelect.select2();
            this.ui.tgtLngsSelect.select2();
            registerValidator(this);
        }

        function registerValidator(instance) {
            Backbone.Validation.bind(instance, {
                valid: function (view, attr) {
                    var div = getParentDiv(attr, view);
                    div.addClass('has-success').removeClass('has-error');
                    div.find('.help-block').html('').addClass('hidden');
                },
                invalid: function (view, attr, error) {
                    var div = getParentDiv(attr, view);
                    div.addClass('has-error').removeClass('has-success');
                    div.find('.help-block').html(error).removeClass('hidden');
                }
            });
        }

        function getParentDiv(attr, view) {
            var currentElement, div;
            currentElement = view.$('#' + attr);
            div = currentElement.closest('.form-group');
            return div;
        }

        function saveJob() {
            var view = this;

            view.model.save({}, {
                success: function () {
                    view.triggerMethod('job:created');
                    app.execute('showNotification', BPMNotification.success("Job created successfully."));
                },
                error: function (model, jqXHR) {
                    app.execute('showNotification', BPMNotification.danger(jqXHR.responseText));
                }
            });
        }

        function close() {
            this.triggerMethod('jobForm:close');
        }
    });