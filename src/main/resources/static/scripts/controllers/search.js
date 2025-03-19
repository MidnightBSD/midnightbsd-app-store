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


        			var pkgs = $scope.searchResponse.content;
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
        
		$scope.$on('$destroy', function finalize() {
			$scope.search = null;
			$scope.realsearch = null;
			$scope.pageCount = null;
			$scope.updateLocation = null;
		});

		$scope.initsearch();
	}]);