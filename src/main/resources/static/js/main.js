require(['js/app', 'js/BPMRouter', 'js/core/controller/BPMController', 'js/core/model/BPMUser'],

    function (app, BPMRouter, BPMController, BPMUser) {

        'use strict';

        $(function () {

            $(this).bind('drop dragover', function (e) {
                e.preventDefault();
            });

            initCurrentUser().done(startApp);

        });

        function initCurrentUser() {
            var user = new BPMUser();
            app.reqres.setHandler('currentUser', function(){
                return user;
            });
            return $.when(user.fetch());
        }

        function startApp() {
            app.router = new BPMRouter({controller: new BPMController()});
            app.start();
        }

    });