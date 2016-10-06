define(
    [
        'js/app',
        'backbone.marionette',
        'js/dashboard/view/FileView'
    ],

    function (app, Marionette, FileView) {

        'use strict';

        return Marionette.CollectionView.extend({

            childView: FileView,

            initialize: function (options) {
                this.collection = options.job.get('sourceFiles');
                this.childViewOptions = function () {
                    return {
                        job: options.job,
                        downloadFileUrl: app.constants.URLS.JOB_SOURCE_FILE_DOWNLOAD,
                        isRemoveFileBtnVisibleFn: function (status) {
                            return !status;
                        }
                    }
                }
            }

        });

    });