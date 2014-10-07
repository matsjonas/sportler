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

    .factory('RestfulResource', function ($resource) {
        return function (url, params, methods) {
            var defaults = {
                update: { method: 'put', isArray: false },
                create: { method: 'post' }
            };

            methods = angular.extend(defaults, methods);

            var resource = $resource(url, params, methods);

            resource.prototype.$save = function () {
                if (!this.id) {
                    // if the instance doesn't have an id,
                    // call the create method that uses a
                    // POST request
                    return this.$create();
                } else {
                    // if the instance do have an id, call
                    // the update method that uses a PUT
                    // request
                    return this.$update();
                }
            };

            return resource;
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

    .controller('ProfileEditor', ['$scope', '$location', '$routeParams', 'RestfulResource', '$window',
        function ($scope, $location, $routeParams, $resource, $window) {

        var Player = $resource("./api/player/:id", { id: '@id' });

        $scope.player = Player.get({ id: 1 });

        $scope.updateProfile = function() {
            $scope.player.$save(function() {
                // ignored
            }, function() {
                $window.alert("Something went wrong! The page will be reloaded...");
                $window.location.reload();
            });
        };

    }])

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

        $scope.deletePlayer = function(player) {
            player.$remove(function() {
                $scope.players = Player.query();
            }, function() {
                $window.alert("Something went wrong! The page will be reloaded...");
                $window.location.reload();
            });
        };

    })

    .controller('TeamController', function ($scope, $location, $routeParams, $resource, $window) {

        var Player = $resource("./api/player/:id", { id: '@id' });
        var Team = $resource("./api/team/:id", { id: '@id' });

        $scope.players = Player.query();
        $scope.teams = Team.query();

        $scope.createNewTeam = function() {
            var team = new Team();
            team.name = $scope.newTeamName;
            team.owner = $scope.newTeamOwner;
            team.$save(function() {
                $scope.newTeamName = null;
                $scope.newTeamOwner = null;
                $scope.players = Player.query();
                $scope.teams = Team.query();
            }, function() {
                $window.alert("Something went wrong! The page will be reloaded...");
                $window.location.reload();
            });
        };

        $scope.deleteTeam = function(team) {
            team.$remove(function() {
                $scope.players = Player.query();
                $scope.teams = Team.query();
            }, function() {
                $window.alert("Something went wrong! The page will be reloaded...");
                $window.location.reload();
            });
        };

    })

    .controller('SeasonController', function ($scope, $location, $routeParams, $resource, $window) {

        var Player = $resource("./api/player/:id", { id: '@id' });
        var Season = $resource("./api/season/:id", { id: '@id' });

        $scope.players = Player.query();
        $scope.seasons = Season.query();

        $scope.createNewSeason = function() {
            var season = new Season();
            season.name = $scope.newSeasonName;
            season.owner = $scope.newSeasonOwner;
            season.$save(function() {
                $scope.newSeasonName = null;
                $scope.newSeasonOwner = null;
                $scope.players = Player.query();
                $scope.seasons = Season.query();
            }, function() {
                $window.alert("Something went wrong! The page will be reloaded...");
                $window.location.reload();
            });
        };

        $scope.deleteSeason = function(season) {
            season.$remove(function() {
                $scope.players = Player.query();
                $scope.seasons = Season.query();
            }, function() {
                $window.alert("Something went wrong! The page will be reloaded...");
                $window.location.reload();
            });
        };

    })

    .controller('GameController', function ($scope, $location, $routeParams, $resource, $window) {

        var Game = $resource("./api/season/:sid/game/:id", { sid: '@seasonId', id: '@id' });
        var Player = $resource("./api/player/:id", { id: '@id' });
        var Team = $resource("./api/team/:id", { id: '@id' });
        var Season = $resource("./api/season/:id", { id: '@id' });

        $scope.games = Game.query({ sid: 0 });
        $scope.players = Player.query();
        $scope.teams = Team.query();
        $scope.seasons = Season.query();

        $scope.createNewGame = function() {
            var game = new Game();
            game.homePlayer = $scope.newGameHomePlayer;
            game.awayPlayer = $scope.newGameAwayPlayer;
            game.homeTeam = $scope.newGameHomeTeam;
            game.awayTeam = $scope.newGameAwayTeam;
            game.homePoints = parseInt($scope.newGameHomePoints);
            game.awayPoints = parseInt($scope.newGameAwayPoints);
            game.$save({ sid: $scope.newGameSeason.id }, function() {
                $scope.newGameSeason = null;
                $scope.newGameHomePlayer = null;
                $scope.newGameAwayPlayer = null;
                $scope.newGameHomeTeam = null;
                $scope.newGameAwayTeam = null;
                $scope.newGameHomePoints = null;
                $scope.newGameAwayPoints = null;
                $scope.games = Game.query({ sid: 0 });
                $scope.players = Player.query();
                $scope.team = Team.query();
                $scope.seasons = Season.query();
            }, function() {
                $window.alert("Something went wrong! The page will be reloaded...");
                $window.location.reload();
            });
        };

        $scope.deleteGame = function(game) {
            game.$remove({ sid: game.season.id }, function() {
                $scope.games = Game.query({ sid: 0 });
                $scope.players = Player.query();
                $scope.team = Team.query();
                $scope.seasons = Season.query();
            }, function() {
                $window.alert("Something went wrong! The page will be reloaded...");
                $window.location.reload();
            });
        };

    })

;