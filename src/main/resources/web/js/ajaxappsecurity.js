var ajaxappsecurity = angular.module('ajaxappsecurity', ['ngResource','ngRoute']);

ajaxappsecurity.factory('httpInterceptor', ['$q', '$injector',
	function($q, $injector) {
		var interceptor = {
			'request': function(config) {
				config.headers['X-Requested-With'] = 'XMLHttpRequest';
				return config;
			},
			'response': function(resp) {
				return resp;
			},
			'responseError': function(rejection) {
				var rootScope = $injector.get('$rootScope');
				var handled = false;
				switch(rejection.status) {
					case 404:
						break;
					case 500:
						break;
					case 901:
						handled = true;
						rootScope.$broadcast('login.failure', rejection.data);
						break;
					case 902:
						handled = true;
						rootScope.$broadcast('access.denied', rejection.data);
						break;
					case 903:
						handled = true;
						rootScope.$broadcast('session.invalid', rejection.data);
						break;
				}
				if (handled) {
					return $q.resolve(rejection.data);
				}
				else {
					return $q.reject(rejection);
				}
			}
		};
		return interceptor;
	}
]);


ajaxappsecurity.config(function($httpProvider) {
	$httpProvider.interceptors.push('httpInterceptor');
});

ajaxappsecurity.run(function($rootScope) {
    $rootScope.$on('login.failure', function(evt, data)  {
    	$rootScope.loginError = true;
    	$rootScope.saveRoute = true;
    	$rootScope.loginErrorMessage = 'Wrong username/password supplied';
    	$location.path('/login');
    });

    $rootScope.$on('session.invalid', function(evt, data)  {
    	$rootScope.loginError = true;
    	$rootScope.saveRoute = true;
    	$rootScope.loginErrorMessage = 'Session timeout, please login again';
    	$location.path('/login');
    });

    $rootScope.$on('access.denied', function(evt, data)  {
    	alert("access denied");
    });
});
