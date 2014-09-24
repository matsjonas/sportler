angular.module('sportlerApp', ['ngRoute'])

    .config(function ($routeProvider) {
        $routeProvider
            .when('/dashboard', {
                controller: 'DashboardController',
                templateUrl: 'assets/templates/dashboard.html'
            })
            .when('/profile', {
                controller: 'ProfileEditor',
                templateUrl: 'assets/templates/profile.html'
            })
            .when('/users', {
                controller: 'UserController',
                templateUrl: 'assets/templates/users.html'
            })
            .when('/teams', {
                controller: 'TeamController',
                templateUrl: 'assets/templates/teams.html'
            })
            .when('/seasons', {
                controller: 'SeasonController',
                templateUrl: 'assets/templates/seasons.html'
            })
            .when('/games', {
                controller: 'GameController',
                templateUrl: 'assets/templates/games.html'
            })
            .otherwise({
                redirectTo: '/dashboard'
            });
    })

    .controller('SidebarController', function($scope, $location) {
        $scope.current = $location.path();

        $scope.activeIf = function(path) {
            return $location.path() == path ? "active" : false;
        };
    })

    .controller('DashboardController', function ($scope, $location, $routeParams) {

    })

    .controller('ProfileEditor', function ($scope, $location, $routeParams) {

    })

    .controller('UserController', function ($scope, $location, $routeParams) {

    })

    .controller('TeamController', function ($scope, $location, $routeParams) {

    })

    .controller('SeasonController', function ($scope, $location, $routeParams) {

    })

    .controller('GameController', function ($scope, $location, $routeParams) {

    })

;