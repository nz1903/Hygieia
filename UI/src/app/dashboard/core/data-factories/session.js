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
				var requestCookies = $cookies.getAll();
				
				var list = {}
	            var count = 0;
	            var keys = Object.keys(requestCookies);
	            _(requestCookies).forEach(function (val) {
	                list[keys[count++]] = val;
	            });
	            
				var req = {
						 method: 'GET',
						 url: '/api/findUser',
						 headers: {
							 'cookiesHeader': angular.toJson(list)
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