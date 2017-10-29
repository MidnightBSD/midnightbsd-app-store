angular.module('wwwApp').controller('MainCtrl', ['$scope', '$http', '$location', 'OperatingSystemService', 'CategoryService', 'ArchitectureService',
    function ($scope, $http, $location, OperatingSystemService, CategoryService, ArchitectureService) {
    'use strict';

    $scope.categories = CategoryService.query();
    $scope.os = OperatingSystemService.query();
    $scope.architectures = ArchitectureService.query();

    $scope.search = function() {
        $location.path('/search').search('keyword', $scope.term);
    }
}]);
