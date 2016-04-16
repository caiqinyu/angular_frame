(function () {
    'use strict';
    define([
        "angular",
        "angular-ui-router",
        // registe func module path
        "functions/appAdmin/FrameController",
        "functions/appAdmin/frameService",
        "functions/appAdmin/frameDirectives"
    ], function (angular) {
        var commonModule = angular.module("common", [
            "common.controllers",
            "common.services",
            "common.directives"
        ]);

        return commonModule;
    });
})(this)