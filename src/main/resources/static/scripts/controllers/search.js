angular.module('wwwApp').controller('SearchCtrl', ['$scope', '$log', '$routeParams', 'SearchService', function ($scope, $log, $routeParams, SearchService) {
    'use strict';

    $scope.term = $routeParams.term;

    $scope.search = function () {
        if (angular.isDefined($scope.term)) {
            $log.info("trying to search");
            $scope.results = SearchService.get({term: $scope.term});
        }
        return false;
    };

    $scope.search();
}]);