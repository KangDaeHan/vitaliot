'use strict';
angular.module('common', [
        'ngRoute',
        'ngCookies',
        'ngSanitize',
        'ui.codemirror',
        'angular-loading-bar',
        'common.templates',
        'common.authentication'
    ])

    .filter('encodeURI', function () {
        return function (input) {
            return encodeURIComponent(input);
        };
    });
