angular.module('wwwApp').controller('OsCtrl', ['$scope', '$routeParams', '$location', '$http', 'PackageService',
    function ($scope, $routeParams, $location, $http, PackageService) {
        'use strict';

        $scope.max = 5;
        $scope.size = 50;
        $scope.ratings = {};
        
        $scope.page = $location.search().page;
        if (typeof $scope.page === 'undefined')
            $scope.page = 1;

        $scope.addRating = function (name, score) {
            console.log("rating " + name + " is " + score);
            var data = {
                packageName: name,
                average: $scope.ratings[name].average
            };
            $http.post('api/package/name/' + name + '/rating', data).success(function (data, status) {
                if (status === 201) {
                    ga('send', 'event', 'Rating', 'Add');
                    return false;
                } else {
                    alert("Unable to save rating.");
                    return false;
                }
            }).error(function () {
                alert("Unable to save rating.");
                return false;
            });
        };

        $scope.load = function() {
            $scope.packages = PackageService.queryByOsAndArch({
                os: $routeParams.os,
                arch: $routeParams.arch,
                page: $scope.page,
                size: $scope.size
            }, function() {
                $scope.totalElements = $scope.packages.totalElements;

                for (var i = 0; i < pkgs.length; i++) {
                    $scope.ratings[pkgs[i].name] = PackageService.getRatingAverage({Name: pkgs[i].name});
                }
            });
        };

        $scope.load();
    }]);
