var EVENTS = {
    TOGGLE_SIDEBAR : 'TOGGLE_SIDEBAR'
};

angular.module('sportlerApp', ['ngRoute', 'ngCookies', 'ngResource'])

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
            .when('/players', {
                controller: 'PlayerController',
                templateUrl: 'assets/templates/players.html'
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

    .controller('ProfileEditor', function ($scope, $location, $routeParams, $resource) {

        var Player = $resource("./api/player/:id", { id: '@id' });

        $scope.player = Player.get({ id: 1 });

    })

    .controller('PlayerController', function ($scope, $location, $routeParams, $resource, $window) {

        var Player = $resource("./api/player/:id", { id: '@id' });

        $scope.players = Player.query();

        $scope.createNewPlayer = function() {
            var player = new Player();
            player.email = $scope.newPlayerEmail;
            player.name = $scope.newPlayerName;
            player.password = $scope.newPlayerPassword;
            player.$save(function() {
                $scope.newPlayerEmail = null;
                $scope.newPlayerName = null;
                $scope.newPlayerPassword = null;
                $scope.players = Player.query();
            }, function() {
                $window.alert("Something went wrong! The page will be reloaded...");
                $window.location.reload();
            });
        };

    })

    .controller('TeamController', function ($scope, $location, $routeParams, $resource) {

        var Team = $resource("./api/team/:id", { id: '@id' });

        $scope.teams = Team.query();

    })

    .controller('SeasonController', function ($scope, $location, $routeParams, $resource) {

        var Season = $resource("./api/season/:id", { id: '@id' });

        $scope.seasons = Season.query();

    })

    .controller('GameController', function ($scope, $location, $routeParams, $resource) {

        var Game = $resource("./api/season/:sid/game/:id", { sid: '@seasonId', id: '@id' });

        $scope.games = Game.query({ sid: 0 });

    })

;