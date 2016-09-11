angular.module('ngRoute').directive('miniProfile', ['$parse', function ($parse) {
    return {
       scope: {
    	   id: '=',
    	   details: '='
       },
       //templateUrl:'view/MiniProfile.html'
       template: '<div><a href="#/user/profile/{{id}}"> <img src="/FamilyNetworkService/family/relationshipGraph/get/profile/picture/{{id}}" class="img-circle" width="50" height="50"/><span>{{details.name}}</span></a></div>'
    };
 }]);