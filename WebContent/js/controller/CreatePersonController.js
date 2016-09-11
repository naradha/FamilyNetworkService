angular.module('ngRoute').controller('createPerson', 
		['$scope', '$http', '$location', 'fileUpload', function($scope, $http, $location, fileUpload) {
	$scope.userInfo = {};
	$scope.userInfo.contactInfo = {};
	$scope.credential = {};
	
	$scope.createPerson = function() {
		$scope.userInfo.contactInfo.email = $scope.credential.username;
		$http.post('http://localhost:8080/FamilyNetworkService/family/relationshipGraph/create/person', 
				{userInfo: $scope.userInfo, credential: $scope.credential}).
	    success(function(data) {
			uploadProfilePicture(data);
	    	$location.url("user/profile/" + data);
	    });
	}
	
	function uploadProfilePicture(personId) {
		fileUpload.uploadFileToUrl($scope.profilePicture, 
				'http://localhost:8080/FamilyNetworkService/family/relationshipGraph/upload/profilePicture/' + personId);
	}
	
}]);
