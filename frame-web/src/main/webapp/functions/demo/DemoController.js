(function () {
    'use strict';
    var currFoldPath = "functions/demo";

    define([
        "underscore",
        currFoldPath + "/demoModule",
        currFoldPath + "/demoService"
    ], function (_, demo) {
        demo.register.controller('DemoController', [
            "$scope",
            "$state",
            "$filter",
            "demoService",
            function ($scope, $state, $filter, demoService) {
                /** Warning!  这个设置，必须得有，决定menu中的ng-include跳转页面 **/
                $scope.params = $state.params

                $scope.testClick = function () {
                    demoService.test();
                }

                $scope.columns = [
                    "",
                    "标识",
                    "用户名",
                    "性别",
                    "操作"
                ];

                $scope.callServer = function(tableState){
                    $scope.isLoading = true;
                    var pagination = tableState.pagination;
                    var start = pagination.start;
                    var number = pagination.number;
                    demoService.gridLoad(tableState).then(function(result){
                        $scope.rowCollection = result;
                        tableState.pagination.numberOfPages = 2;
                        $scope.isLoading = false;
                    } , function(){
                        //error
                    });
                }

            }]);
    });
})
(this);
