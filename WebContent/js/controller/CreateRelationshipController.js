angular.module('ngRoute').controller('createRelationship', 
		['$scope', '$http', '$routeParams', '$location', function($scope, $http, $routeParams, $location) {
			
	$scope.personId = $routeParams.personId;
	
	$scope.searchUsers = function() {
		$http.get('http://localhost:8080/FamilyNetworkService/family/relationshipGraph/search/' + $scope.searchString).
		    success(function(data) {
		    	$scope.searchResult = data;
	})};
	
	$scope.createRelationship = function(relatedPersonId, relationshipType) {
		$http.put('http://localhost:8080/FamilyNetworkService/family/relationshipGraph/create/relationship/from/'
			+ $scope.personId + '/to/' + relatedPersonId + '/relationshipType/' + relationshipType).
		    success(function(data) {
			$location.url("/graph/" + $scope.personId); 
	})};
	
}]);
