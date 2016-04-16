/**
 * Frame app
 */
(function () {
    'use strict';
    define([
        "angular",
        "angularAMD",
        "angular-ui-router",
        //
        "functions/appAdmin/commonModule",
        "functions/demo/demoModule",
        "functions/dataview/dataviewModule"
    ], function (angular, angularAMD) {
        var app = angular.module("frameApp", ["ui.router",
            "common",
            // customer module add below
            "demo",
            "dataview"
        ])

        app.config(function ($stateProvider, $locationProvider, $controllerProvider, $provide, $compileProvider , $filterProvider) {
            // 添加手动注入支持(虽然app支持动态注入，但不建议这么做，理论上每个模块应该有自己独立的module)
            app.register = {
                controller : $controllerProvider.register,
                factory : $provide.factory,
                service : $provide.service,
                filter: $filterProvider.register,
                directive : $compileProvider.directive
            };
            // 注册菜单state
            $stateProvider.state("_menu", angularAMD.route({
                url: "/common/menu",
                templateUrl: "functions/appAdmin/template/Menu.tmp.html",
                params: {
                    templateUrl: "",
                    controllerNm: "",
                    controllerPath : ""
                },
                controllerProvider: function ($stateParams) {
                    return $stateParams.controllerNm;
                },
                resolve: {
                    load: ["$q", "$rootScope", "$stateParams", function ($q, $rootScope, $stateParams) {
                        // 动态加载controller
                        var loadController = $stateParams.controllerPath;
                        var defered = $q.defer();
                        require([loadController], function () {
                            $rootScope.$apply(function () {
                                defered.resolve();
                            });
                        });
                        return defered.promise;
                    }]
                },
            }));

            // 去除URL中的#，需要base tag支持
            //$locationProvider.html5Mode(true);
        });

        app.config(function ($httpProvider) {
            $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
            $httpProvider.defaults.withCredentials = true;
        });

        return app;
    });
})
(this)


