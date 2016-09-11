angular.module('ngRoute').controller('userProfile', 
		['$scope', '$http', '$routeParams', function($scope, $http, $routeParams) {
	$scope.personId = $routeParams.personId;
	
	$http.get('http://localhost:8080/FamilyNetworkService/family/relationshipGraph/get/user/details/' + $scope.personId).
	success(function(data) {
		$scope.user = data;
	});
}]);