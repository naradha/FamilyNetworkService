angular.module('ngRoute').controller('search', ['$scope', '$http', '$routeParams', '$location', function($scope, $http, $routeParams, $location) {
	$scope.search = $routeParams.search;	
	
	$http.get('http://localhost:8080/FamilyNetworkService/family/relationshipGraph/search/' + $scope.search).
    success(function(data) {
    	$scope.searchResult = data;
    });
	
	$scope.searchUsers = function() {
		$location.url("/search/" + $scope.search);
		
	}

}]);