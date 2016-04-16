(function () {
    'use strict';
    var currFoldPath = "functions/dataview";

    define([
            currFoldPath + "/dataviewModule"
        ], function (dataviewModule) {

           dataviewModule.register.directive("echartDomain" , [
               "$timeout",function($timeout){
                return {
                    restrict : "E",
                    replace : true,
                    controller : "DataviewController",
                    template : "<div></div>",
                    scope : {},
                    link: function(scope , element , attr){
                        var id = element.attr("id");
                        var width = element.attr("width");
                        var height = element.attr("height");
                        if(!id){
                            id = "ed"+scope.$id;
                            element.attr("id" , id);
                        }
                        if(!width){
                            width = "100%";
                        }
                        element.width(width);
                        if(!height){
                            height = document.body.scrollHeight;
                        }
                        element.height(height);
                        // 广播渲染完成事件
                        $timeout(function() {
                            scope.$emit('echartDomainFinished' , id);
                        });
                    }
                }
            }]);

        }
    );
})(this);