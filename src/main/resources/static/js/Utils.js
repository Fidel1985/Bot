define([],
    function () {

        'use strict';

        return {
            buildUrl: function (urlPattern, params) {
                var paramName, paramValue, url;
                url = urlPattern;
                for (paramName in params) {
                    if (params.hasOwnProperty(paramName)) {
                        paramValue = params[paramName];
                        url = url.replace('{' + paramName + '}', paramValue)
                    }
                }
                return url
            }
        }
    });

