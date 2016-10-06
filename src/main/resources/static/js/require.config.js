var require = {
    baseUrl: '../',
    paths: {
        "text": 'lib/requirejs-text/text',
        "jquery": 'lib/jquery/jquery.min',
        "jquery.ui": 'lib/jquery-ui/jquery-ui',
        "jquery.ui.widget": 'lib/jquery-file-upload/js/jquery.ui.widget',
        "jquery.file.upload": 'lib/jquery-file-upload/js/jquery.fileupload',
        "jquery.fileDownload": 'lib/jquery.fileDownload/js/jquery.fileDownload',
        "select2": 'lib/select2/select2',
        "underscore": 'lib/underscore/underscore-min',
        "backbone": 'lib/backbone/backbone-min',
        "backbone.marionette": 'lib/marionette/backbone.marionette',
        "backbone.babysitter": 'lib/backbone.babysitter/backbone.babysitter',
        "backbone.wreqr": 'lib/backbone.wreqr/backbone.wreqr',
        "backbone.stickit": 'lib/backbone.stickit/backbone.stickit',
        "backbone.validation":'lib/backbone-validation/backbone-validation',
        "bootstrap": 'lib/bootstrap/js/bootstrap.min'
    },
    shim: {
        "jquery": {
            exports: "$"
        },
        "jquery.ui": {
            "deps": ['jquery']
        },
        "jquery.file.upload": {
            "deps": ['jquery', 'jquery.ui.widget']
        },
        "jquery.fileDownload": {
            "deps": ['jquery']
        },
        "select2": {
            "deps": ['jquery']
        },
        "bootstrap": {
            "deps": ['jquery']
        },
        "underscore": {
            "exports": "_"
        },
        "backbone": {
            "deps": ['underscore'],
            "exports": "Backbone"
        },
        "backbone.marionette": {
            "deps": ['backbone', 'backbone.babysitter', 'backbone.wreqr'],
            "exports": "Marionette"
        },
        "backbone.stickit": {
            "deps": ['backbone', 'underscore', 'jquery']
        },
        "backbone.validation":{
            "deps":['backbone', 'underscore']
        }
    }
};