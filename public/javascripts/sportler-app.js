var EVENTS = {
    TOGGLE_SIDEBAR : 'TOGGLE_SIDEBAR'
};

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
            if (angular.isArray(callbacks)) {
                for (var i = 0; i < callbacks.length; i++) {
                    callbacks[i](eventArgs);
                }
            }
        };

    })

    .controller('SidebarController', function($scope, $location, EventService) {

        $scope.state = {
            hideSidebar : false
        };

        $scope.activeIf = function(path) {
            return $location.path() == path ? "active" : false;
        };

        EventService.on(EVENTS.TOGGLE_SIDEBAR, function() {
            $scope.state.hideSidebar = !$scope.state.hideSidebar;
        });
    })

    .controller('NavbarController', function($scope, EventService) {

        $scope.state = {
            shrinkHeader : false
        };

        $scope.toggleSidebar = function() {
            $scope.state.shrinkHeader = !$scope.state.shrinkHeader;
            EventService.emit(EVENTS.TOGGLE_SIDEBAR);
        };
    })

    .controller('MainContentController', function ($scope, EventService) {

        $scope.state = {
            fullSize : false
        };

        EventService.on(EVENTS.TOGGLE_SIDEBAR, function() {
            $scope.state.fullSize = !$scope.state.fullSize;
        });
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