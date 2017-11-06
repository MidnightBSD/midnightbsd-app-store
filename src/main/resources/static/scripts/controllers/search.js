angular.module('wwwApp').controller('SearchCtrl', ['$scope', '$log', '$routeParams', '$location', '$http', 'SearchService', 'PackageService',
    function ($scope, $log, $routeParams, $location, $http, SearchService, PackageService) {
        'use strict';

        $scope.max = 5;
        $scope.ratings = {};
        
        $scope.keyword = '';
   		$scope.searchTerm = '';
        $scope.processing = false;
   		$scope.showResults = false;
   		$scope.pageSize = 10;

		$scope.KeywordsHeading = '';

		$scope.initsearch = function () {
			$scope.keyword = $location.search().keyword;
			$scope.setPage(1); // call realsearch
		};

		$scope.updateLocation = function (keyword) {
			$location.search({keyword: keyword});
		};

		$scope.search = function () {
            $scope.processing=true;
			//check for min length of 3
			if(typeof $scope.keyword === 'undefined' || $scope.keyword.length < 3) {
                $log.info('keyword is invalid');
                $scope.processing=false;

				return;
			}

			$scope.updateLocation($scope.keyword);
			$scope.realsearch();
		};

		$scope.pageCount = -1;
		$scope.page = 1;
		$scope.totals = 0;

		$scope.setPage = function(pg) {
			$scope.page = pg;
			$scope.realsearch();
		};

		$scope.realsearch = function () {
			//check for min length of 3
			if (typeof $scope.keyword === 'undefined' || $scope.keyword.length < 3) {
				$log.info('keyword is invalid');
                $scope.processing=false;
				return;
			}

			$scope.pageCount = -1; // reset to hide view all message on fresh search

			$log.info('Starting search on ' + $scope.keyword + ' with page ' + $scope.page);
			var kw = $scope.keyword.replace('\'', '');

			$scope.searchResponse = SearchService.get(
				{
					term: kw,
					page: ($scope.page -1),
					size: $scope.pageSize
				},
				function() {
					if (typeof $scope.searchResponse === 'undefined' || $scope.searchResponse.totalElements === 0) {
						$scope.pageCount = 0;
						$scope.showResults = false;
                        $scope.processing=false;
						$scope.totals = 0;
						return;
					}

					$scope.totals = $scope.searchResponse.totalElements;
					$scope.pageCount = $scope.searchResponse.totalPages;
					$log.debug('Page Count: ' + $scope.pageCount);
					$scope.showResults = true;
                    $scope.processing=false;


                    for (var i = 0; i < pkgs.length; i++) {
                        $scope.ratings[pkgs[i].name] = PackageService.getRatingAverage({Name: pkgs[i].name});
                    }
                },
				function(reason) {
					$log.error(reason);
					if (typeof $scope.searchResponse === 'undefined' || $scope.searchResponse.totalElements === 0) {
						$log.debug('No Results found due to error or 404');
						$scope.pageCount = 0;
						$scope.totals = 0;
						$scope.showResults = false;
                        $scope.processing=false;
					}
				});
        };

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
        
		$scope.$on('$destroy', function finalize() {
			$scope.search = null;
			$scope.realsearch = null;
			$scope.pageCount = null;
			$scope.updateLocation = null;
		});

		$scope.initsearch();
	}]);