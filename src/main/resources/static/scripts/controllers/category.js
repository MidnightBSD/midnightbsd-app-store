angular.module('wwwApp').controller('CategoryCtrl', ['$scope', '$routeParams', '$location', 'CategoryService', 'PackageService',
    function ($scope, $routeParams, $location, CategoryService, PackageService) {
        'use strict';

        $scope.category = CategoryService.get({Id: $routeParams.id}, function (cat) {
            $scope.packages = PackageService.queryByCategoryName({ Name: cat.name });
        })
    }]);
