angular.module('wwwApp',
        [
            'ui.bootstrap.pagination',
            'ui.bootstrap.rating',
            'ngRoute',
            'ngResource',
            'ngCookies',
            'ngRoute',
            'ngSanitize'
        ])
        .config(function ($routeProvider, $locationProvider) {
            'use strict';

            $routeProvider
                    .when('/', {
                        templateUrl: 'views/main.html',
                        controller: 'MainCtrl'
                    })
                    .when('/category/:id', {
                        templateUrl: 'views/category.html',
                        controller: 'CategoryCtrl'
                    })
                    .when('/license/:license', {
                        templateUrl: 'views/license.html',
                        controller: 'LicenseCtrl'
                    })
                    .when('/os/:os/arch/:arch', {
                        templateUrl: 'views/os.html',
                        controller: 'OsCtrl'
                    })
                    .when('/privacy', {
                        templateUrl: 'views/privacy.html',
                        controller: 'PrivacyCtrl'
                    })
                    .when('/search', {
                        templateUrl: 'views/search.html',
                        controller: 'SearchCtrl'
                    })
                    .otherwise({
                        redirectTo: '/'
                    });

            $locationProvider.hashPrefix('!');
        });