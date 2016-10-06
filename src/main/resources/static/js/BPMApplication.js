define(
    [
        'backbone',
        'backbone.marionette',
        'js/Constants',
        'js/Utils'
    ],
    function (Backbone, Marionette, constants, utils) {

        return Marionette.Application.extend({

            constants: constants,
            utils: utils,

            onStart: function () {
                if (Backbone.history) {
                    Backbone.history.start();
                }
            }

        });

    });