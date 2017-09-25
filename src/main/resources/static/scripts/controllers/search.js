angular.module('wwwApp').controller('SearchCtrl', ['$scope', '$log', 'SearchService', function ($scope, $log, SearchService) {
    'use strict';

    $scope.term = '';

    $scope.search = function () {
        $log.info("trying to search");
        $scope.results = SearchService.get({term: $scope.term});
    };
}]);