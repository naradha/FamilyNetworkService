angular.module('ngRoute').service('authenticationService', 
		['$http', '$location', function ($http, $location) {
			
	var service = {};
		 
    service.login = login;
    service.logout = logout;
 
    return service;
			
	function login(username, password, callback) {
		$http.post('http://localhost:8080/FamilyNetworkService/family/authentication/login', { username: username, password: password })
        .success(function (response) {
            // store username and token in local storage to keep user logged in between page refreshes
            //$localStorage.currentUser = { username: username, token: response };

            // add jwt token to auth header for all requests made by the $http service
            $http.defaults.headers.common.Authorization = 'Bearer ' + response.token;

            // execute callback with true to indicate successful login
            callback(response.id);
            
        })
        .error(function (reponse) {
        	callback(null);
        });
    }

    function logout() {
        // remove user from local storage and clear http auth header
        //delete $localStorage.currentUser;
        $http.defaults.headers.common.Authorization = '';
        $location.url("login");
    }
}]);