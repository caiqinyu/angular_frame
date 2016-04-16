/**
 * Created by caiqinyu on 2015/11/19.
 */
(function () {
    'use strict';
    var currFoldPath = "functions/appAdmin";

    define(["angular", "json"], function (angular, JSON) {
        var services = angular.module("common.services", []);

        services.service("loginService", [
            "$http",
            "$q",
            "$window",
            function ($http, $q, $window) {
                /**
                 * 登录
                 * @param erpNo
                 * @param password
                 * @returns {*}
                 */
                this.doLogin = function (erpNo, password) {
                    var deffer = $q.defer();
                    $http({
                        method: "post",
                        url: "logon?dt=" + new Date().getTime(),
                        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        data: {
                            erpNo: erpNo,
                            password: password
                        }
                    }).success(function (data, status, headers, config) {
                        var token = data.token;
                        //$window.sessionStorage["token"] = JSON.stringify(token);
                        deffer.resolve(data);
                    }).error(function () {
                        deffer.reject("登录失败");
                    });
                    return deffer.promise;
                };

                /**
                 * 登出
                 */
                this.doLogout = function () {
                    var deffer = $q.defer();
                    $http({
                        method: "post",
                        url: "logout?dt=" + new Date().getTime()
                    }).success(function (data, status, headers, config) {
                        deffer.resolve(data);
                    }).error(function () {
                        deffer.reject("登出失败");
                    });
                    return deffer.promise;
                }
            }]);

        services.service("funcService", [
            "$http",
            "$q",
            function ($http, $q) {
                this.getFuncs = function () {
                    var deffer = $q.defer();
                    $http({
                        method: "post",
                        url: "getFuncs?dt=" + new Date().getTime(),
                        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        data: {
                            userId: "caiqinyu"  // require.getParameterMap获取
                        }
                    }).success(function (data, status, headers, config) {
                        if (data
                            && data.funcs) {
                            deffer.resolve(data.funcs);
                        } else {
                            deffer.resolve([]);
                        }
                    }).error(function () {
                        deffer.reject("获取菜单信息失败");
                    });
                    return deffer.promise;
                };
            }])

        return services;
    });
})(this);
