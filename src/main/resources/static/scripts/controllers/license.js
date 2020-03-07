angular.module('wwwApp').controller('LicenseCtrl', ['$scope', '$routeParams', '$location', '$http', 'PackageService', 'LicenseService',
    function ($scope, $routeParams, $location, $http, PackageService, LicenseService) {
        'use strict';

        $scope.max = 5;
        $scope.size = 50;
        $scope.ratings = {};

        $scope.page = $location.search().page;
        if (typeof $scope.page === 'undefined')
            $scope.page = 0;
        
        $scope.addRating = function (name, score) {
            console.log("rating " + name + " is " + score);
            var data = {
                packageName: name,
                average: $scope.ratings[name].average
            };
            $http.post('api/package/name/' + name + '/rating', data)
                    .then(function onSuccess(response) {
                        // Handle success
                        var data = response.data;
                        var status = response.status;
                        if (status === 201) {
                            ga('send', 'event', 'Rating', 'Add');
                            return false;
                        } else {
                            alert("Unable to save rating.");
                            return false;
                        }
                    }).catch(function onError(response) {
                alert("Unable to save rating.");
                return false;
            });
        };

        $scope.license = LicenseService.queryByName({ Name: $routeParams.license});

        $scope.load = function() {
            $scope.packages = PackageService.queryByLicense({
                license: $routeParams.license,
                page: $scope.page,
                size: $scope.size
            }, function(pkgs) {
                $scope.totalElements = $scope.packages.totalElements;

                for (var i = 0; i < pkgs.length; i++) {
                    $scope.ratings[pkgs[i].name] = PackageService.getRatingAverage({Name: pkgs[i].name});
                }

            });
        };

        $scope.load();
    }]);
