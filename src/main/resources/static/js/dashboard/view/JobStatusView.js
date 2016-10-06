define(
    [
        'js/app',
        'backbone.marionette',
        'backbone.stickit'
    ],

    function (app, Marionette) {

        'use strict';

        var statusBtnClassesMap, JobStatus;

        JobStatus = app.constants.JOB.STATUS;
        statusBtnClassesMap = {};
        statusBtnClassesMap[JobStatus.PENDING] = 'btn btn-default glyphicon glyphicon-play';
        statusBtnClassesMap[JobStatus.IN_PROGRESS] = 'btn btn-default glyphicon glyphicon-pause';
        statusBtnClassesMap[JobStatus.COMPLETED] = 'glyphicon glyphicon-ok';
        statusBtnClassesMap['UNDEFINED'] = 'glyphicon glyphicon-exclamation-sign';

        return Marionette.ItemView.extend({

            template: _.template('<span aria-hidden="true"></span>'),
            ui: {
                'statusBtn': 'span'
            },
            events: {
                "click @ui.statusBtn": updateJobWithNextStatus
            },
            bindings: {
                'span': {
                    attributes: [{
                        name: 'class',
                        observe: 'status',
                        onGet: getStatusBtnClasses
                    }, {
                        name: 'title',
                        observe: 'status'
                    }]
                }
            },
            onRender: function () {
                this.stickit();
            }

        });

        function updateJobWithNextStatus() {

            var newStatus;

            newStatus = getNextStatus(this.model.get('status'));
            newStatus && updateJobStatus(this.model, newStatus);

        }

        function getNextStatus(currentStatus) {

            var jobStatusFlow;

            jobStatusFlow = app.request('jobStatusFlow');
            return jobStatusFlow.getNextStatus(currentStatus);

        }

        function updateJobStatus(model, newStatus) {

            model.save(
                {'status': newStatus},
                {
                    wait: true,
                    error: function (model, response) {
                        alert(response);
                    }
                });

        }

        function getStatusBtnClasses(jobStatus) {

            return statusBtnClassesMap[jobStatus] || statusBtnClassesMap['UNDEFINED'];

        }

    });