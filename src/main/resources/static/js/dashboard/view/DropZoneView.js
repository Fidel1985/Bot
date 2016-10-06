define(
    [
        'js/app',
        'backbone.marionette',
        'js/dashboard/model/FileModel',
        'text!js/dashboard/template/DropZoneTemplate.html',
        'js/core/model/BPMNotification',
        'jquery.file.upload'
    ],

    function (app, Marionette, FileModel, template, BPMNotification) {

        'use strict';

        return Marionette.ItemView.extend({

            template: _.template(template),
            ui: {
                dropFileZone: '#dropFileZone',
                fileInput: '[data-drop-zone]'
            },
            onRender: function () {
                initFileUpload(this);
            }

        });

        function initFileUpload(view) {

            view.ui.fileInput.fileupload({
                dataType: 'json',
                dropZone: view.ui.dropFileZone,
                autoUpload: false,
                add: onFileAdd(view)
            });

        }

        function onFileAdd(view) {

            return function (e, data) {

                var file, fileModel;

                if (isValid(data.files)) {
                    file = data.files[0];
                    if (isFileNameExist(file, view.collection)) {
                        app.execute('showNotification',
                            BPMNotification.warning("File '" + file.name + "' is already added. " +
                                "If you want to overwrite it please delete the existed one first."));

                    } else {
                        fileModel = new FileModel({name: file.name, size: file.size});
                        fileModel.file = file;
                        view.collection.add(fileModel);
                    }
                }

            }

        }

        function isFileNameExist(file, collection) {
            return collection.find(function (fileModel) {
                return fileModel.get('name') === file.name;
            })
        }

        function isValid(files) {

            return !isEmptyFolder(files) && !isEmptyFile(files[0]);

        }

        function isEmptyFolder(files) {
            var isEmptyFolder = false;

            if (files.length == 0) {
                app.execute('showNotification', BPMNotification.danger("Folder is empty."));
                isEmptyFolder = true;
            }
            return isEmptyFolder;
        }

        function isEmptyFile(file) {
            var isEmptyFile = false;

            if (file.size == 0) {
                app.execute('showNotification', BPMNotification.danger("File is empty."));
                isEmptyFile = true;
            }
            return isEmptyFile;
        }

    });