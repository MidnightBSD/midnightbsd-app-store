angular.module('wwwApp').controller('LicenseCtrl', ['$scope', '$routeParams', '$location', 'PackageService', 'LicenseService',
    function ($scope, $routeParams, $location, PackageService, LicenseService) {
        'use strict';

        $scope.size = 50;

        $scope.page = $location.search().page;
        if (typeof $scope.page === 'undefined')
            $scope.page = 0;

        $scope.license = LicenseService.queryByName({ Name: $routeParams.license});

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
