var app = angular.module('FamilyNetwork', ['ngRoute']);

app.config(['$routeProvider', 
    function($routeProvider) {
    	$routeProvider.
    	when('/login',{
        	templateUrl: 'view/login.html', 
        	controller: 'login',
        	controllerUrl: 'js/controller/LoginController.js'
        })
        .when('/graph/:personId',{
        	templateUrl: 'view/FamilyGraph.html', 
        	controller: 'familyGraph',
        	controllerUrl: 'js/controller/FamilyGraphController.js'
        })
        .when('/search',{
        	templateUrl: 'view/search.html', 
        	controller: 'search',
        	controllerUrl: 'js/controller/SearchController.js'
        })
        .when('/search/:search',{
        	templateUrl: 'view/search.html', 
        	controller: 'search',
        	controllerUrl: 'js/controller/SearchController.js'
        })
        .when('/create/person',{
        	templateUrl: 'view/CreatePerson.html', 
        	controller: 'createPerson',
        	controllerUrl: 'js/controller/CreatePersonController.js'
        })
        .when('/create/relationship/:personId',{
        	templateUrl: 'view/CreateRelationship.html', 
        	controller: 'createRelationship',
        	controllerUrl: 'js/controller/CreateRelationshipController.js'
        })
        .when('/user/profile/:personId',{
        	templateUrl: 'view/UserProfile.html', 
        	controller: 'userProfile',
        	controllerUrl: 'js/controller/UserProfileController.js'
        })
        .otherwise({
            redirectTo: '/login'
        });
    }]
);