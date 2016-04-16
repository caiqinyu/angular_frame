(function () {
    'use strict';
    var currFoldPath = "functions/appAdmin";

    define([
        "angular",
        (currFoldPath + "/frameService"),
        "underscore"
    ], function (angular, frameService, _) {
        var controllers = angular.module("common.controllers", ["common.services"]);

        controllers.controller("frameController", [
            "$scope",
            "$state",
            "$window",
            "loginService",
            "funcService",
            function ($scope, $state, $window, loginService, funcService) {
                $scope.selectFuncId = "";

                $scope.title = "业务功能";

                funcService.getFuncs().then(function (funcs) {
                    // 刷新dom
                    $scope.funcs = funcs;
                }, function (errorMsg) {
                    // error
                    console.log(errorMsg);
                });

                // 执行登录
                $scope.doLogin = function (data) {
                    loginService.doLogin().then(function (data) {
                        $window.location.href = "index";
                        console.log("登录成功");
                    }, function () {
                        console.log("登录失败啦");
                    });
                }

                // 执行登出
                $scope.doLogout = function () {
                    loginService.doLogin().then(function (data) {
                        $window.location.href = "login";
                        console.log("登出成功");
                    }, function () {
                        console.log("登出失败啦");
                    });
                }

                // 点击菜单事件
                $scope.funcClick = function (func) {
                    if (func) {
                        $scope.selectFuncId = func.funcId;
                        $state.go("_menu", {
                            templateUrl: func.templateUrl,
                            controllerNm: func.controllerNm,
                            controllerPath: func.controllerPath
                        },{
                            reload: true
                        });
                    }
                }
            }]);

        return controllers;
    });
})
(this);
