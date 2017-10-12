angular.module('wwwApp').factory('CategoryService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/category/:Id', { Id: '@Id' });
}]);