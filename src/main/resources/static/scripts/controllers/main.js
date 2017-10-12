angular.module('wwwApp').controller('MainCtrl', ['$scope', '$http', 'OperatingSystemService', 'CategoryService',
    function ($scope, $http, OperatingSystemService, CategoryService) {
    'use strict';

    $scope.categories = CategoryService.query();
    $scope.os = OperatingSystemService.query();
}]);
