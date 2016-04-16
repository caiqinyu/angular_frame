(function () {
    'use strict';
    var currFoldPath = "functions/appAdmin";

    define([
            "angular",
            (currFoldPath + "/commonModule")
        ], function (angular , directives) {
            var directives = angular.module("common.directives" , []);

            directives.directive("loginForm" , function(){
                return {
                    restrict : "E",
                    controller : "frameController",
                    templateUrl : currFoldPath + "/template/LoginForm.tmp.html"
                }
            });

            directives.directive("sideBarItem", function () {
                return {
                    restrict: "E",
                    controller: "frameController",
                    templateUrl: currFoldPath + "/template/SideBarItem.tmp.html"
                }
            });

            directives.directive("userMenu" , function(){
                return {
                    restrict : "E",
                    replace : true,
                    controller : "frameController",
                    templateUrl : currFoldPath + "/template/UserMenu.tmp.html"
                }
            });

            return directives;
        }
    );
})(this);