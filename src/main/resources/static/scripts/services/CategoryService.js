angular.module('wwwApp').factory('CategoryService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/category/:Id', {Id: '@Id', Name: '@Name'},
            {
                'queryByName': {
                    method: 'GET',
                    isArray: true,
                    url: '/api/category/name/:Name'
                }
            });
}]);