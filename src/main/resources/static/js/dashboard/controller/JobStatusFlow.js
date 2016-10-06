define(
    ['js/app'],

    function (app) {

        'use strict';

        var nextStatusMap;

        return function () {

            var JobStatus, nextStatusMap;

            JobStatus = app.constants.JOB.STATUS;

            nextStatusMap = {};

            nextStatusMap[JobStatus.PENDING] = JobStatus.IN_PROGRESS;
            nextStatusMap[JobStatus.IN_PROGRESS] = JobStatus.PENDING;

            this.getNextStatus = function (currentStatus) {
                var nextStatus;
                nextStatus = nextStatusMap[currentStatus];
                nextStatus || console.log("Unrecognised Job Status '" + currentStatus + "'.");
                return nextStatus;
            }

        };

    });

