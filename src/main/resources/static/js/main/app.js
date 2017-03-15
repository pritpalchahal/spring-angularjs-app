var app = angular.module('app', ['ngRoute','ngResource','ngCookies']);
app.config(function($routeProvider){
    $routeProvider
	    .when('/login',{
	        templateUrl: '/views/login.html',
	        controller: 'LoginController',
	        controllerAs: 'vm'
	    })
	    .when('/register',{
	        templateUrl: '/views/register.html',
	        controller: 'RegisterController',
	        controllerAs: 'vm'
	    })
        .when('/ping',{
            templateUrl: '/views/ping.html',
            controller: 'PingController'
        })
        .otherwise(
            { redirectTo: '/login'}
        );
});

