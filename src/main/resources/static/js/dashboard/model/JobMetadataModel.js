define(
    [
        'js/app',
        'backbone',
        'js/dashboard/collection/FileCollection',
        'backbone.validation'
    ],

    function (app, Backbone, FileCollection) {

        'use strict';

        return Backbone.Model.extend({

            url: app.constants.URLS.JOBS,

            defaults: function () {
                return {
                    id: null,
                    name: '',
                    description: '',
                    status: null,
                    sourceLanguage: null,
                    targetLanguages: null,
                    sourceFiles: new FileCollection(),
                    targetFiles: new FileCollection()
                };
            },

            validate: function () {
                if (!this.isValid(true)) {
                    return "not valid user.";
                }
            },

            validation: {
                name: [{
                    required: true,
                    msg: 'Please, enter the job name'
                }, {
                    maxLength: 80,
                    msg: 'Job name length should be equals or less than 80 characters'
                }],
                description: {
                    maxLength: 255,
                    msg: 'Job description length should be equals or less than 255 characters'
                },
                sourceLanguage: {
                    required: true,
                    msg: 'Please, select a source language'
                },
                targetLanguages: function (value) {
                    if (!(value instanceof Array && value.length)) {
                        return 'Please, select at lest one target language';
                    }
                },
                sourceFiles: function (value) {
                    if (value.length != 1) {
                        return 'Please, select single source file';
                    }
                }
            },

            parse: function (response) {
                parseCollection.call(this, response, 'sourceFiles', app.constants.URLS.JOB_SOURCE_FILE);
                parseCollection.call(this, response, 'targetFiles', app.constants.URLS.JOB_TARGET_FILE);
                return response;
            },

            sync: function (method, model, options) {
                if(method === "create"){
                    options = _.extend(options || {}, {
                        contentType: false,
                        data: getFormData(this)
                    });
                }

                return Backbone.sync(method, model, options);
            },

            saveTargetFiles: function (options) {
                var url = app.utils.buildUrl(app.constants.URLS.JOB_TARGET_FILE, {jobId: this.id});

                $.ajax({

                    url: url,
                    contentType: false,
                    cache: false,
                    processData: false,
                    data: getTargetFiles(this),
                    type: 'POST'

                }).done(onDone(this, options)).fail(onFail(options));
            },

            hasUnsavedTargetFiles: function () {
                return this.get('targetFiles').find(function (targetFile) {
                    return targetFile.isNew();
                });
            }
        });

        function parseCollection(response, collectionAttrName, url) {

            var models, modelCollection;

            models = response[collectionAttrName];
            modelCollection = this.get(collectionAttrName);
            if (modelCollection) {
                modelCollection.set(models);
            } else {
                modelCollection = new FileCollection(models);
            }
            modelCollection.url = app.utils.buildUrl(url, {jobId: response.id});
            response[collectionAttrName] = modelCollection;

        }

        function getFormData(model) {

            var formData;

            formData = new FormData();
            formData.append('job', new Blob([JSON.stringify(model.toJSON())], {
                type: "application/json"
            }));
            model.get('sourceFiles').each(function (sourceFile) {
                formData.append('sourceFiles', sourceFile.file);
            });
            return formData;

        }

        function getTargetFiles(model) {

            var formData;

            formData = new FormData();

            model.get('targetFiles').each(function (targetFile) {
                targetFile.isNew() && formData.append('targetFiles', targetFile.file);

            });
            return formData;

        }

        function onDone(model, options) {

            return function (data, textStatus, jqXHR) {

                model.set(model.parse(data));
                model.trigger('sync');
                options.success && options.success(data, textStatus, jqXHR);

            }
        }


        function onFail(options) {

            return function (jqXHR, textStatus, errorThrown) {

                options.error && options.error(jqXHR, textStatus, errorThrown)

            }
        }

    });

