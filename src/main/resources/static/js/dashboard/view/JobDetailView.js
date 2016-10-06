define(
    [
        'js/app',
        'backbone.marionette',
        'js/dashboard/view/DropZoneView',
        'js/dashboard/view/SourceFileCollectionView',
        'js/dashboard/view/TargetFileCollectionView',
        'text!js/dashboard/template/JobDetailTemplate.html',
        'js/core/model/BPMNotification',
        'backbone.stickit',
        'select2',
        'jquery.file.upload'
    ],

    function (app, Marionette, DropZoneView, SourceFileCollectionView, TargetFileCollectionView, template, BPMNotification) {

        'use strict';


        return Marionette.LayoutView.extend({

            template: _.template(template),
            regions: {
                sourceFileCollection: '#sourceFileCollection',
                targetFileDropZone: '#targetFileDropZone',
                targetFileCollection: '#targetFileCollection'
            },
            ui: {
                finishBtn: '#finishBtn',
                uploadBtn: '#uploadBtn'
            },
            events: {
                'click @ui.finishBtn': finishJob,
                'click @ui.uploadBtn': saveTargetFiles
            },
            bindings: {
                '#sourceLanguage': {
                    observe: 'sourceLanguage',
                    onGet: function (sourceLanguage) {
                        return sourceLanguage.tag;
                    }
                },
                '#targetLanguages': {
                    observe: 'targetLanguages',
                    onGet: function (targetLanguages) {
                        return _.pluck(targetLanguages, 'tag').join(", ");
                    }
                },
                '#finishBtn': {
                    classes: {
                        hidden: {
                            observe: 'status',
                            onGet: function (status) {
                                return status === app.constants.JOB.STATUS.COMPLETED;
                            }
                        }
                    }
                }
            },
            initialize: function () {
                this.listenTo(this.model.get('targetFiles'), 'add remove', updateUploadButtonState.bind(this));
            },
            onRender: function () {
                this.stickit();
                updateUploadButtonState.call(this);
            },
            onBeforeShow: function () {
                this.showChildView('sourceFileCollection', new SourceFileCollectionView({job: this.model}));
                this.showChildView('targetFileCollection', new TargetFileCollectionView({job: this.model}));
                isJobCompleted(this.model) && this.showChildView('targetFileDropZone', new DropZoneView({collection: this.model.get('targetFiles')}));
            }
        });

        function updateUploadButtonState() {
            if (this.model.hasUnsavedTargetFiles()) {
                this.ui.uploadBtn.removeClass('hidden');
            } else {
                this.ui.uploadBtn.addClass('hidden');
            }
        }

        function finishJob() {
            var view = this;

            view.model.set('status', app.constants.JOB.STATUS.COMPLETED);
            view.model.save([], {
                success: function () {
                    app.execute('showNotification', BPMNotification.success("Job " + view.model.get('id') + " completed."));
                    view.getRegion('targetFileDropZone').empty();
                },
                error: function (jqXHR) {
                    app.execute('showNotification', BPMNotification.danger(jqXHR.responseText));
                }
            });
        }

        function saveTargetFiles() {
            var uploadBtn;

            uploadBtn = this.ui.uploadBtn;
            uploadBtn.prop('disabled', true);
            this.model.saveTargetFiles({
                success: function () {
                    uploadBtn.prop('disabled', false);
                },
                error: function (jqXHR) {
                    app.execute('showNotification', BPMNotification.danger(jqXHR.responseText));
                    uploadBtn.prop('disabled', false);
                }
            });
        }

        function isJobCompleted(job) {
            return job.get('status') !== app.constants.JOB.STATUS.COMPLETED;
        }

    });