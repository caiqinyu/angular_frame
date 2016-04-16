/**
 * Frame config
 */
(function () {
    'use strict';
    require.config({
        baseUrl: top.contextPath + "/",
        packages: [
            {
                name: 'echarts',
                location: 'common/components/echarts',
                main: 'echarts'
            },
            {
                name: 'zrender',
                location: 'common/components/zrender', // zrender与echarts在同一级目录
                main: 'zrender'
            }
        ],
        paths: {
            "angular": "common/angular1/angular",
            "angular-ui-router": "common/angular1/angular-ui-router.min",
            "angularAMD": "common/angular-amd/angularAMD",
            "ngload": "common/angular-amd/ngload",
            "domReady": "common/requirejs/domReady",
            "underscore": "common/tools/underscore-min",
            "json": "common/tools/json2.min",
            // ui
            "ui-bootstrap": "common/components/ui-bootstrap/ui-bootstrap-custom-tpls-0.14.3",
            "smart-table": "common/components/smart-table/smart-table",
            "lrDragNDrop": "common/components/smart-table/lrDragNDrop",
            "echarts/chart/map":"common/components/echarts/chart/map",
            //own components
            "app": "functions/app",
            "startup": "functions/startup"
        },
        shim: {
            "angular": {
                exports: "angular"
            },
            "angular-ui-router": {
                deps: ["angular"]
            },
            "angularAMD": {
                deps: ["angular"]
            },
            "ui-bootstrap": {
                deps: ["angular"]
            },
            "smart-table": {
                deps: ["angular"]
            },
            "ngload": {
                deps: ["angularAMD"]
            },
            "underscore": {
                exports: "_"
            },
            "json": {
                exports: "JSON2"
            }
        },
        deps: ["startup"],
        urlArgs: "dateTime=" + (new Date().getTime()) // 防止读取缓存，调试用
    });
})();

