var EVENTS = {
    TOGGLE_SIDEBAR : 'TOGGLE_SIDEBAR'
};

angular.module('sportlerApp', ['ngRoute', 'ngCookies'])

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

    .service('SharedSettings', function($cookies) {

        this.settings = {
            sidebarVisible: !$cookies.sidebarVisible || $cookies.sidebarVisible == 'true'
        };

        this.toggleSidebarVisible = function() {
            this.settings.sidebarVisible = !this.settings.sidebarVisible;
            $cookies.sidebarVisible = this.settings.sidebarVisible;
        };

    })

    .controller('SidebarController', function($scope, $location, SharedSettings) {

        $scope.sharedSettings = SharedSettings.settings;

        $scope.state = {

        };

        $scope.activeIf = function(path) {
            return $location.path() == path ? "active" : false;
        };
    })

    .controller('NavbarController', function($scope, SharedSettings) {

        $scope.sharedSettings = SharedSettings.settings;

        $scope.state = {

        };

        $scope.toggleSidebar = function() {
            SharedSettings.toggleSidebarVisible();
        };
    })

    .controller('MainContentController', function ($scope, SharedSettings) {

        $scope.sharedSettings = SharedSettings.settings;

        $scope.state = {

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