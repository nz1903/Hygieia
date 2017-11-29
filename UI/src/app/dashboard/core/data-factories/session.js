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

    Session.$inject = ['$http', '$window', 'userService', '$cookies'];
    function Session($http, $window, userService, $cookies) {
    	return {
    		updateSession : updateSession
    	}
    	
    	function updateSession() {
			if(!userService.isAuthenticated()) {
				/*return $http.get("/api/findUser")*/
				var req = {
						 method: 'GET',
						 url: '/api/findUser',
						 headers: {
						   'HTTP_USER': $cookies.get('HTTP_USERC'),
						   'givenName':$cookies.get('givenNameC'),
						   'sn':$cookies.get('snC'),
						   'mail':$cookies.get('mailC'),
						   'displayName':$cookies.get('DisplayNameC'),
						   'initials':$cookies.get('initialsC')
						 }
				}

				return $http(req).then(function scss(response) {
		        	$window.localStorage.token = response.headers()['x-authentication-token'];
		            return true;
		        }, function err(error) {
		        	return "empty";
		        });
			} else {
				return "authenticated";
			}
		}
    	
    }
})();