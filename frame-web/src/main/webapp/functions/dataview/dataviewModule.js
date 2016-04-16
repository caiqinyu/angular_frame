/**
 * Created by caiqinyu on 2015/12/3.
 */
(function(){
    define(["angular" , "ui-bootstrap"] , function(angular){
        var dataview =  angular.module("dataview" , [
            "ui.bootstrap"
        ]);
        dataview.config(function($controllerProvider, $provide, $compileProvider , $filterProvider){
            // 添加动态注册支持
            dataview.register = {
                controller : $controllerProvider.register,
                factory : $provide.factory,
                service : $provide.service,
                filter: $filterProvider.register,
                directive : $compileProvider.directive
            };
        });
        return dataview;
    })
})()