angular.module('wwwApp').factory('PackageService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/package/:Id', {Id: '@Id', Name: '@Name'},
            {
                'getByName': {
                    method: 'GET',
                    isArray: false,
                    url: '/api/package/name/:Name'
                },
                'queryByCategoryName': {
                    method: 'GET',
                    isArray: true,
                    url: '/api/package/category/:Name'
                }
            });
}]);