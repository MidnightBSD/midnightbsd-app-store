angular.module('wwwApp').controller('CategoryCtrl', ['$scope', '$routeParams', '$location', 'CategoryService',
    function ($scope, $routeParams, $location, CategoryService) {
        'use strict';

        $scope.category = CategoryService.get({id:  $routeParams.id})
    }]);
