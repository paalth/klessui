var klessuiapp = angular.module("klessuiapp", ['ngMaterial', 'ui.ace', 'ui.router', 'md.data.table', 'ngResource']); 

klessuiapp.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
  var handlersState = {
    name: 'handlers',
    url: '/handlers',
    templateUrl: 'views/handlers.html'
  }

  var handlerCreateState = {
    name: 'handlerCreate',
    url: '/handlerCreate',
    templateUrl: 'views/createHandler.html'
  }
  
  var frontendsState = {
    name: 'frontends',
    url: '/frontends',
    templateUrl: 'views/frontends.html'
  }

  var frontendTypesState = {
    name: 'frontendTypes',
    url: '/frontendTypes',
    templateUrl: 'views/frontendTypes.html'
  }
  
  var buildersState = {
    name: 'builders',
    url: '/builders',
    templateUrl: 'views/builders.html'
  }

  var helpState = {
    name: 'help',
    url: '/help',
    templateUrl: 'views/help.html'
  }

  var aboutState = {
    name: 'about',
    url: '/about',
    templateUrl: 'views/about.html'
  }
  
  $stateProvider.state(handlersState);
  $stateProvider.state(handlerCreateState);
  $stateProvider.state(frontendsState);
  $stateProvider.state(frontendTypesState);
  $stateProvider.state(buildersState);
  $stateProvider.state(helpState);
  $stateProvider.state(aboutState);
  
  $urlRouterProvider.otherwise('/handlers');
}]);

klessuiapp.controller("ApplicationController", [ '$scope', function($scope) { 
	//, $location	
}]);

klessuiapp.controller("FrontendsController", [ '$scope', 'frontendService', function($scope, frontendService) {	
	
	$scope.selected = [];
	
	frontendService.getFrontends($scope);
	
}]);

klessuiapp.controller("FrontendTypesController", [ '$scope', 'frontendTypeService', function($scope, frontendTypeService) {	
	
	$scope.selected = [];
	
	frontendTypeService.getFrontendTypes($scope);
	
}]);

klessuiapp.controller("BuildersController", [ '$scope', 'builderService', function($scope, builderService) {	
	
	$scope.selected = [];
	
	builderService.getBuilders($scope);
	
}]);

klessuiapp.controller("CreateHandlerController", [ '$scope', 'handlerService', 'builderService', 'frontendService', function($scope, handlerService, builderService, frontendService) {	
	
	builderService.getBuilders($scope);
	frontendService.getFrontends($scope);
		
	$scope.createHandler = function(newHandler) {
		handlerService.createHandler(newHandler, $scope);
	}
}]);

klessuiapp.controller("HandlersController", [ '$scope', 'handlerService', 'builderService', 'frontendService', function($scope, handlerService, builderService, frontendService) {	
	
	$scope.selected = [];
	
	handlerService.getHandlers($scope);
		
}]);

klessuiapp.factory("handlerService", [ '$resource', function ($resource) {
	return new HandlerService($resource);
}])

function HandlerService(resource) {
	
	this.getHandlers = function(scope) {
		console.log("Start getHandlers");
		
		var handlerService = resource("api/handler");
		handlerService.get(function(response) {
			console.log(response.status);
			scope.handlers = response.eventHandlerInformation;
		});
		
		console.log("End getHandlers");
	}
	
	this.createHandler = function(newHandler, scope) {
		console.log("Start createHandler");

		var handlerService = resource("api/handler");
		handlerService.save(newHandler, function(response) {
			console.log(response);
		});
		
		console.log("End createHandler");
	}
}

klessuiapp.factory("builderService", [ '$resource', function ($resource) {
	return new BuilderService($resource);
}])

function BuilderService(resource) {
	
	this.getBuilders = function(scope) {
		console.log("Start getBuilders");
		
		var builderService = resource("api/builder");
		builderService.get(function(response) {
			console.log(response.status);
			scope.builders = response.eventHandlerBuilderInformation;
		});
		
		console.log("End getBuilders");
	}
	
	this.createBuilder = function(newBuilder, scope) {
		console.log("Start createBuilder");

		var builderService = resource("api/builder");
		builderService.save(newBuilder, function(response) {
			console.log(response);
		});
		
		console.log("End createBuilder");
	}
}

klessuiapp.factory("frontendService", [ '$resource', function ($resource) {
	return new FrontendService($resource);
}])

function FrontendService(resource) {
	
	this.getFrontends = function(scope) {
		console.log("Start getFrontends");
		
		var frontendService = resource("api/frontend");
		frontendService.get(function(response) {
			console.log(response.status);
			scope.frontends = response.frontendInformation;
		});
		
		console.log("End getFrontends");
	}
	
	this.createFrontend = function(newFrontend, scope) {
		console.log("Start createFrontend");

		var frontendService = resource("api/frontend");
		frontendService.save(newFrontend, function(response) {
			console.log(response);
		});
		
		console.log("End createFrontend");
	}
}

klessuiapp.factory("frontendTypeService", [ '$resource', function ($resource) {
	return new FrontendTypeService($resource);
}])

function FrontendTypeService(resource) {
	
	this.getFrontendTypes = function(scope) {
		console.log("Start getFrontendTypes");
		
		var frontendTypeService = resource("api/frontendtype");
		frontendTypeService.get(function(response) {
			console.log(response.status);
			scope.frontendTypes = response.frontendTypeInformation;
		});
		
		console.log("End getFrontendTypes");
	}
	
	this.createFrontendType = function(newFrontendType, scope) {
		console.log("Start createFrontendType");

		var frontendTypeService = resource("api/frontendtype");
		frontendTypeService.save(newFrontendType, function(response) {
			console.log(response);
		});
		
		console.log("End createFrontendType");
	}
}