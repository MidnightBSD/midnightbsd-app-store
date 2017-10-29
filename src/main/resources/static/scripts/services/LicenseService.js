angular.module('wwwApp').factory('LicenseService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/license/:Id', {Id: '@Id', Name: '@Name'},
            {
                'queryByName': {
                    method: 'GET',
                    isArray: true,
                    url: '/api/license/name/:Name'
                }
            });
}]);