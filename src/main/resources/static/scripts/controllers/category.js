angular.module('wwwApp').controller('CategoryCtrl', ['$scope', '$routeParams', '$location', '$http', 'CategoryService', 'PackageService',
    function ($scope, $routeParams, $location, $http, CategoryService, PackageService) {
        'use strict';

        $scope.max = 5;
        $scope.ratings = {};

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
                    console.error('Error saving rating:', response);
                    window.alert('Unable to save rating. Please try again later.');
                });
        };

        $scope.category = CategoryService.get({Id: $routeParams.id}, function (cat) {
            $scope.packages = PackageService.queryByCategoryName({Name: cat.name},
                function (pkgs) {
                    if (pkgs === 'undefined' || pkgs.length === 0) {
                        return;
                    }

                    pkgs.forEach(function (pkg) {
                        $scope.ratings[pkg.name] = PackageService.getRatingAverage({Name: pkg.name},
                            function () {
                            },
                            function () {
                                $scope.ratings[pkg.name] = {
                                    average: 0
                                };
                            }
                        );
                    });
                });
        });
    }]);
