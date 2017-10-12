angular.module('wwwApp').factory('OperatingSystemService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/os/:Id', { Id: '@Id' });
}]);