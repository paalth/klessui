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
  
  var handlersDetailState = {
	  name: 'handlersDetail',
      url: '/handler/:eventHandlerName',
      views: {
        '@': {
          templateUrl: 'views/handlerDetail.html',
          controller: 'HandlerDetailsController'
        }
      },
    }
  
  var frontendsState = {
    name: 'frontends',
    url: '/frontends',
    templateUrl: 'views/frontends.html'
  }

  var frontendCreateState = {
	name: 'frontendCreate',
	url: '/frontendCreate',
	templateUrl: 'views/createFrontend.html'
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
  $stateProvider.state(handlersDetailState);
  $stateProvider.state(frontendsState);
  $stateProvider.state(frontendCreateState);
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
	
	$scope.deleteFrontends = function() {
		console.log("Currently selected frontends: ");
		var selectedLen = $scope.selected.length;
		var i;
		for (i = 0; i < selectedLen; i++) {
			console.log($scope.selected[i]);
			frontendService.deleteFrontend($scope.selected[i]);
			
			var frontendsLen = $scope.frontends.length;
			console.log("Current frontends length = " + frontendsLen);
			var j, frontendsIndex = 0;
			for (j = 0; j < frontendsLen; j++) {
				if ($scope.frontends[j].eventHandlerFrontendName === $scope.selected[i].eventHandlerFrontendName) {
					frontendsIndex = j;
				}
			}
			console.log("Removing frontend at index = " + frontendsIndex);
			$scope.frontends.splice(frontendsIndex, 1);
		}
		$scope.selected = [];
	}
}]);

klessuiapp.controller("CreateFrontendController", [ '$scope', 'frontendService', 'frontendTypeService', function($scope, frontendService, frontendTypeService) {	
	
	frontendTypeService.getFrontendTypes($scope);
	
	$scope.createFrontend = function(newFrontend) {
		frontendService.createFrontend(newFrontend, $scope);
	}
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
	
	$scope.newHandler = {};
	$scope.newHandler.sourceCode = "";
	
	$scope.editorLanguages = ['golang', 'java', 'python'];
	$scope.currentEditorlanguage = 'java';
	
	builderService.getBuilders($scope);
	frontendService.getFrontends($scope);
	
	$scope.aceLoaded = function(_editor) {
		$scope.editor = _editor;
	};
		  
	$scope.createHandler = function(newHandler) {
		handlerService.createHandler(newHandler, $scope);
	}
	
	$scope.changeEditorLanguage = function() {
		console.log("Current editor language = " + $scope.currentEditorlanguage);
		$scope.editor.getSession().setMode('ace/mode/' + $scope.currentEditorlanguage);
	}
	
	$scope.createEmptyHandler = function() {
		console.log("Create empty handler here, current language = " + $scope.currentEditorlanguage);
		if ($scope.currentEditorlanguage === 'golang') {
			$scope.newHandler.sourceCode = "package eventhandler\n\n" + 
										  "import (\n" +
										  "	\"fmt\"\n\n" +
										  "	kl \"github.com/paalth/kless/pkg/interface/klessgo\"\n" +
										  ")\n\n" +
										  "//EventHandler dummy for now\n" +
										  "type EventHandler struct {\n" +
										  "}\n\n" +
										  "//Handler the actual event handler that does nothing in this case\n" +
										  "func (t EventHandler) Handler(c *kl.Context, resp *kl.Response, req *kl.Request) {\n" +
										  "	fmt.Printf(\"Inside funcHandler\\n\")\n" +
										  "}\n";
		} else if ($scope.currentEditorlanguage === 'java') {
			$scope.newHandler.sourceCode = "package io.kless;\n\n" + 
										   "class EventHandler1 implements EventHandlerInterface {\n\n" + 
										   "    public Response eventHandler(Context context, Request req) {\n" + 
										   "        System.out.println(\"Inside event handler...\");\n\n" + 
										   "        return null;\n" + 
										   "    }\n\n" + 
										   "}\n";
		} else if ($scope.currentEditorlanguage === 'python') {
			$scope.newHandler.sourceCode = "import sys\n\n" + 
										   "class EventHandler1:\n" + 
                                            "    def eventHandler(self, context, request, response):\n" + 
                                            "        sys.stdout.write(\"Inside event handler example 1\")\n" + 
                                            "        sys.stdout.flush()\n"; 
		}
	}
}]);

klessuiapp.controller("HandlerDetailsController", [ '$scope', 'handlerService', 'builderService', 'frontendService', '$location', function($scope, handlerService, builderService, frontendService, $location) {	

	var absUrl = $location.absUrl();
	var i = absUrl.lastIndexOf('/') + 1;
	var handlerName = absUrl.substring(i, absUrl.length);

	console.log("View handler detail, handler = " + handlerName);

	$scope.eventHandlerName = handlerName;
}]);

klessuiapp.controller("HandlersController", [ '$scope', '$state', 'handlerService', 'builderService', 'frontendService', function($scope, $state, handlerService, builderService, frontendService) {	
	
	$scope.selected = [];
	
	handlerService.getHandlers($scope);
	
	$scope.deleteHandlers = function() {
		console.log("Currently selected handlers: ");
		var selectedLen = $scope.selected.length;
		var i;
		for (i = 0; i < selectedLen; i++) {
			console.log($scope.selected[i]);
			handlerService.deleteHandler($scope.selected[i]);
			
			var handlersLen = $scope.handlers.length;
			console.log("Current handlers length = " + handlersLen);
			var j, handlersIndex = 0;
			for (j = 0; j < handlersLen; j++) {
				if ($scope.handlers[j].eventHandlerName === $scope.selected[i].eventHandlerName) {
					handlersIndex = j;
				}
			}
			console.log("Removing handler at index = " + handlersIndex);
			$scope.handlers.splice(handlersIndex, 1);
		}
		$scope.selected = [];
	}
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
	
	this.deleteHandler = function(handler, scope) {
		console.log("Start deleteHandler");

		var handlerService = resource("api/handler/:id");
		handlerService.delete({ id: handler.eventHandlerName }, function(response) {
			console.log(response);
		});
		
		console.log("End deleteHandler");
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
	this.deleteFrontend = function(frontend, scope) {
		console.log("Start deleteFrontend");

		var frontendService = resource("api/frontend/:id");
		frontendService.delete({ id: frontend.eventHandlerFrontendName }, function(response) {
			console.log(response);
		});
		
		console.log("End deleteFrontend");
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