/**
 * Session Factory - This service updates the session variables
 * for the user by making a call to our API to create the user details of which it got from
 * Ping SSO.
 *
 * @return {[type]} [description]
 */
(function() {
  'use strict';

  angular
    .module(HygieiaConfig.module + '.core')
      .factory('Session', Session);

    Session.$inject = ['$http', '$window', 'userService'];
    function Session($http, $window, userService) {
    	return {
    		updateSession : updateSession
    	}
    	
    	function updateSession() {
			if(!userService.isAuthenticated()) {
				return $http.get("/api/findUser")
		        .then(function mySuccess(response) {
		        	$window.localStorage.token = response.headers()['x-authentication-token'];
		            return true;
		        }, function myError(error) {
		        	return "empty";
		        });
			} else {
				return "authenticated";
			}
		}
    	
    }
})();