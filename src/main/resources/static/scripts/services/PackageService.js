angular.module('wwwApp').factory('PackageService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/package/:Id', {Id: '@Id', Name: '@Name', os: '@os', arch: '@arch', license: '@license'},
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
                },
                'queryByLicense': {
                    method: 'GET',
                    isArray: true,
                    url: '/api/package/license/:license'
                },
                'queryByOsAndArch': {
                    method: 'GET',
                    isArray: false,
                    url: '/api/package/os/:os/arch/:arch'
                }
            });
}]);