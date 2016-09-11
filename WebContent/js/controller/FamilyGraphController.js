angular.module('ngRoute').controller('familyGraph', 
		['$scope', '$http', '$routeParams', function($scope, $http, $routeParams) {
	var edgesList = [];
	var userList = [];
	$scope.personId = $routeParams.personId;
	
	$scope.loadGraph = function() {
	
		$http.get('http://localhost:8080/FamilyNetworkService/family/relationshipGraph/getRelations/' 
				+ $scope.personId + '/depth/3').
	    success(function(data) {
	    	
	        for (u in data) {
	        	updateUserList(data[u].userInfo);
	        	updateRelationships(data[u].relationships);
	        }
	        
	        function updateUserList(userInfo) {
	        	if (!userInfo) {
	        		return;
	        	}
	        	
	        	var user = {};
	        	user.id = u;
	        	user.label = userInfo.name;
	        	user.level = userInfo.level;
	        	user.shape = 'circularImage';
	        	user.image = 'family/relationshipGraph/get/profile/picture/' + u;
	        	
	        	userList.push(user);
	        }
	        
	        function updateRelationships(relationships) {
	        	if (!relationships) {
	        		return;
	        	}
	        	
	        	for (i=0; i< relationships.length; i++) {
	        		var edge = {};
	        		edge.from = u
	        		edge.to = relationships[i].relatedPersonId;
	        		edgesList.push(edge);
	        	}
	        }
	    	
	    	var nodes = new vis.DataSet(userList);
	    	
	    	 // create an array with edges
	    	 var edges = new vis.DataSet(edgesList);
	    	
	    	 // create a network
	    	 var container = document.getElementById('mynetwork');
	    	
	    	 // provide the data in the vis format
	    	 var data = {
	    	     nodes: nodes,
	    	     edges: edges,
	    	 };
	    	 var options = {
	    	 	layout :{
	    	 		hierarchical:true
	    	 	},
	    	 	width: '100%'
	    	 };
	    	
	    	 // initialize your network!
	    	 var network = new vis.Network(container, data, options);
	    });
	}
	
}]);