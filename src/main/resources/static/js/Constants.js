define([],
    function () {

        'use strict';

        return {
            URLS: {
                JOBS: 'dashboard/jobs',
                JOB_SOURCE_FILE: 'dashboard/jobs/{jobId}/source',
                JOB_SOURCE_FILE_DOWNLOAD: 'dashboard/jobs/{jobId}/source/{fileId}',
                JOB_TARGET_FILE: 'dashboard/jobs/{jobId}/target',
                JOB_TARGET_FILE_DOWNLOAD: 'dashboard/jobs/{jobId}/target/{fileId}',
                USERS: 'configuration/users',
                LANGUAGES: 'dashboard/languages',
                AUTHORITIES: 'configuration/authority',
                CURRENT_USER: 'session/user'
            },
            JOB: {
                STATUS: {
                    PENDING: 'PENDING',
                    IN_PROGRESS: 'IN_PROGRESS',
                    COMPLETED: 'COMPLETED'
                }
            },
            NOTIFICATION_TYPE: {
                SUCCESS: "success",
                INFO: "info",
                WARNING: "warning",
                DANGER: "danger"
            }
        }
    });
