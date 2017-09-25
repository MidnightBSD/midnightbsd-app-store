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
                    .when('/entry/:entryId', {
                        templateUrl: 'views/entry.html',
                        controller: 'EntryCtrl'
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
                    .when('/sitemap', {
                        templateUrl: 'views/sitemap.html',
                        controller: 'SitemapCtrl'
                    })
                    .otherwise({
                        redirectTo: '/'
                    });

            $locationProvider.hashPrefix('!');
        });