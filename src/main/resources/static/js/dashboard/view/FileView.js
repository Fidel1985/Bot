define(
    [
        'js/app',
        'backbone.marionette',
        'text!js/dashboard/template/FileTemplate.html',
        'js/core/model/BPMNotification',
        'jquery.fileDownload'
    ],

    function (app, Marionette, template, BPMNotification) {

        'use strict';

        return Marionette.ItemView.extend({

            template: _.template(template),
            ui: {
                removeFileBtn: '[data-remove-file]',
                downloadFileBtn: '[data-download-file]'
            },
            events: {
                'click @ui.removeFileBtn': removeFile,
                'click @ui.downloadFileBtn': downloadFile
            },
            bindings: {
                '[data-name]': 'name',
                '[data-download-file]': {
                    observe: 'id',
                    visible: true
                }
            },
            onRender: function () {
                this.stickit();
                this.stickit(this.options.job, getJobBindings(this.options.isRemoveFileBtnVisibleFn));
            },
            initialize: function (options) {
                this.downloadFileUrl = app.utils.buildUrl(options.downloadFileUrl, {
                    jobId: options.job.get('id'),
                    fileId: this.model.get('id')
                });
            }
        });

        function removeFile() {
            this.model.destroy({
                wait: true,
                error: function (model, response) {
                    app.execute('showNotification', BPMNotification.danger(response.responseText));
                }
            });
        }

        function downloadFile() {
            $.fileDownload(this.downloadFileUrl, {
                failCallback: function (response) {
                    app.execute('showNotification', BPMNotification.danger(response));
                }
            });
        }

        function getJobBindings(isRemoveFileBtnVisibleFn) {
            return {
                '[data-remove-file]': {
                    observe: 'status',
                    visible: isRemoveFileBtnVisibleFn
                }
            }
        }
    });