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

    .service('EventService', function () {

        this.TOGGLE_SIDEBAR = 'TOGGLE_SIDEBAR';

        var _registeredCallbacks = {};

        this.on = function (event, callback) {
            if (!_registeredCallbacks[event]) {
                _registeredCallbacks[event] = [];
            }
            if (typeof callback == "function") {
                _registeredCallbacks[event].push(callback);
            }
        };

        this.emit = function (event, eventArgs) {
            var callbacks = _registeredCallbacks[event];
            if ($.isArray(callbacks)) {
                for (var i = 0; i < callbacks.length; i++) {
                    callbacks[i](eventArgs);
                }
            }
        };

    })

    .controller('SidebarController', function($scope, $location, EventService) {
        $scope.current = $location.path();

        $scope.activeIf = function(path) {
            return $location.path() == path ? "active" : false;
        };

        EventService.on(EventService.TOGGLE_SIDEBAR, function() {
            console.log('SidebarController on it!');
        });
    })

    .controller('NavbarController', function($scope, EventService) {
        $scope.toggleSidebar = function() {
            EventService.emit(EventService.TOGGLE_SIDEBAR);
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