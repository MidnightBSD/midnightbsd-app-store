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
            // eslint-disable-next-line no-console
            console.log(JSON.stringify({
                action: 'Rating added',
                package: name,
                score: score,
                timestamp: new Date().toISOString()
            }));
            var data = {
                packageName: name,
                average: score  // Use the actual score instead of $scope.ratings[name].average
            };
            $http.post('api/package/name/' + name + '/rating', data)
                .then(function onSuccess(response) {
                    // Handle success
                    if (response.status === 201) {
                        // eslint-disable-next-line no-undef
                        gtag('event', 'add_rating', {
                            'event_category': 'Rating',
                            'event_label': name
                        });
                        // Update the local rating
                        $scope.ratings[name] = { average: score };
                    } else {
                        window.alert('Unable to save rating. Unexpected status: ' + response.status);
                    }
                }).catch(function onError(response) {
                // eslint-disable-next-line no-console
                console.error('Error saving rating:', response);
                window.alert('Unable to save rating. Please try again later.');
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
