angular.module('wwwApp').controller('MainCtrl', ['$scope', '$http', '$location', 'OperatingSystemService',
    'CategoryService', 'ArchitectureService', 'LicenseService',
    function ($scope, $http, $location, OperatingSystemService, CategoryService, ArchitectureService, LicenseService) {
    'use strict';

    $scope.categories = CategoryService.query();
    $scope.os = OperatingSystemService.query();
    $scope.architectures = ArchitectureService.query();
    $scope.licenses = LicenseService.query();

    $scope.search = function() {
        $location.path('/search').search('keyword', $scope.term);
    }
}]);
