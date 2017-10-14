angular.module('wwwApp',
        [
            'ui',
            'ui.bootstrap.pagination',
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
                    .when('/entry', {
                        templateUrl: 'views/entry.html',
                        controller: 'EntryCtrl'
                    })
                    .when('/privacy', {
                        templateUrl: 'views/privacy.html',
                        controller: 'PrivacyCtrl'
                    })
                    .when('/search/:term', {
                        templateUrl: 'views/search.html',
                        controller: 'SearchCtrl'
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