/**
 * Created by caiqinyu on 2015/12/1.
 */
(function () {
    'use strict';

    var currFoldPath = "functions/demo";

    define([
        currFoldPath + "/demoModule",
    ], function (demo) {
        demo.register.service("demoService", [
            "$window",
            "$http",
            "$q",
            function ($window , $http , $q) {
                this.test = function () {
                    $window.location.href = "login.html";
                }

                // 查询列表信息
                this.gridLoad = function(params){
                    var pager = params.pagination;
                    var deffer = $q.defer();
                    $http({
                        method: "post",
                        url: "getUsers?dt=" + new Date().getTime(),
                        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        data: {
                            number : pager.number,
                            start : pager.start,
                        }
                    }).success(function (data, status, headers, config) {
                        if (data
                            && data.users) {
                            deffer.resolve(data.users);
                        } else {
                            deffer.resolve([]);
                        }
                    }).error(function () {
                        deffer.reject("获取列表信息失败");
                    });
                    return deffer.promise;
                }
            }]);
    });
})()