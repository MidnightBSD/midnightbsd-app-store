angular.module('wwwApp').controller('CategoryCtrl', ['$scope', '$routeParams', '$location', '$http', 'CategoryService', 'PackageService',
    function ($scope, $routeParams, $location, $http, CategoryService, PackageService) {
        'use strict';

        $scope.max = 5;
        $scope.ratings = {};

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

        $scope.category = CategoryService.get({Id: $routeParams.id}, function (cat) {
            $scope.packages = PackageService.queryByCategoryName({Name: cat.name},
                    function (pkgs) {
                        if (pkgs === 'undefined' || pkgs.length === 0)
                            return;
                        
                        for (var i = 0; i < pkgs.length; i++) {
                            var n = pkgs[i].name;
                            $scope.ratings[n] = PackageService.getRatingAverage({Name: n},
                                    function() {}, function(n) {
                                        // fail case
                                        $scope.ratings[n] = {
                                            average: 0
                                        }
                                    });
                        }
                    });
        })
    }]);
