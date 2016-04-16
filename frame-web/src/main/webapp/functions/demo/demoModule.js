/**
 * Created by caiqinyu on 2015/12/3.
 */
(function(){
    define(["angular" , "ui-bootstrap" , "smart-table" , "lrDragNDrop"] , function(angular){
        var demo =  angular.module("demo" , [
            "ui.bootstrap" ,
            "smart-table",
            "lrDragNDrop"
        ]);
        demo.config(function($controllerProvider, $provide, $compileProvider , $filterProvider){
            // 添加动态注册支持
            demo.register = {
                controller : $controllerProvider.register,
                factory : $provide.factory,
                service : $provide.service,
                filter: $filterProvider.register,
                directive : $compileProvider.directive
            };
        });
        return demo;
    })
})()