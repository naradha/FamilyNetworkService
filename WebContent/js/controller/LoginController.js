angular.module('ngRoute').controller('login', 
		['$scope', '$location', 'authenticationService', function($scope, $location, authenticationService) {
	
	$scope.login = function() {
		if ($scope.form.$invalid) {
			return;
		}
		
		authenticationService.login($scope.id, $scope.password, function (result) {
            if (result) {
                $location.path('/user/profile/' + result);
            } else {
                $scope.error = 'Username or password is incorrect';
            }
        });
	}
}]);