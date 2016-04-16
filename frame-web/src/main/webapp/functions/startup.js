/**
 * startup
 */
(function(){
    'use strict';
    define([
        "require",
        "angular",
        "angularAMD",
        "app",
        "angular-ui-router"
    ], function (require, angular, angularAMD,  app) {
        require(["domReady!"], function (document) {
            angularAMD.bootstrap(app , document);
        })
    });
})(this);
