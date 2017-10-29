angular.module('wwwApp').controller('LicenseCtrl', ['$scope', '$routeParams', '$location', 'PackageService',
    function ($scope, $routeParams, $location, PackageService) {
        'use strict';

        $scope.size = 50;

        $scope.page = $location.search().page;
        if (typeof $scope.page == 'undefined')
            $scope.page = 1;

        $scope.load = function() {
            $scope.packages = PackageService.queryByLicense({
                license: $routeParams.license,
                page: $scope.page,
                size: $scope.size
            }, function() {
                $scope.totalElements = $scope.packages.totalElements;
            });
        };

        $scope.load();
    }]);
