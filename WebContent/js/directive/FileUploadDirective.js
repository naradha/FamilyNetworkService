angular.module('ngRoute').directive('fileUpload', ['$parse', function ($parse) {
    return {
       scope: {
    	   personId: '@'
       }
    };
 }]);