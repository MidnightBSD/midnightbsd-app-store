angular.module('wwwApp').factory('ArchitectureService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/architecture/:Id', { Id: '@Id' });
}]);