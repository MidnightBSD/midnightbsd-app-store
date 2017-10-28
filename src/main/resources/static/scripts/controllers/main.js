angular.module('wwwApp').controller('MainCtrl', ['$scope', '$http', '$location', 'OperatingSystemService', 'CategoryService',
    function ($scope, $http, $location, OperatingSystemService, CategoryService) {
    'use strict';

    $scope.categories = CategoryService.query();
    $scope.os = OperatingSystemService.query();

    $scope.search = function() {
        $location.path('/search?keyword=' + $scope.term);
    }
}]);
