angular.module('wwwApp',
        [
            'ui',
            'ui.bootstrap.buttons',
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
                    .when('/os/:os/arch/:arch', {
                        templateUrl: 'views/os.html',
                        controller: 'OsCtrl'
                    })
                    .when('/entry', {
                        templateUrl: 'views/entry.html',
                        controller: 'EntryCtrl'
                    })
                    .when('/privacy', {
                        templateUrl: 'views/privacy.html',
                        controller: 'PrivacyCtrl'
                    })
                    .when('/search', {
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