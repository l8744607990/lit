<!DOCTYPE html>
<html lang="en">
<head>
    <title>lit admin</title>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="content-type" content="text/html;charset=UTF-8">

    <link rel="shortcut icon" type="image/x-icon" href="favicon.ico">
    <link rel="stylesheet" href="http://at.alicdn.com/t/font_1156831_xn4orqpa3ee.css">
    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/styles/lit-common.css">
    <link rel="stylesheet"
          href="${springMacroRequestContext.contextPath}/libs/element/2.4.5/themes/${Application.skin!'cyan'}/index.css">
    <link rel="stylesheet"
          href="${springMacroRequestContext.contextPath}/styles/themes/aui-${Application.skin!'cyan'}.css">
    <link id="J_elementTheme" rel="stylesheet">
    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/styles/aui-index.css">
    <script type="text/javascript">
        let contextPath = '${springMacroRequestContext.contextPath}'
        let singlePage = false
    </script>
</head>
<body>
<div id="app">
    <app></app>
</div>

<script src="https://cdn.bootcss.com/require.js/2.3.6/require.min.js"></script>
<!-- 面包屑 -->
<script type="text/html" id="app-breadcrumb-template">
    <!--<div></div>-->
    <div class="aui-main__hd">
        <el-breadcrumb separator="/">
            <el-breadcrumb-item>
                <i class="ic ichome"></i>
            </el-breadcrumb-item>
            <el-breadcrumb-item v-if="title">{{title}}</el-breadcrumb-item>
            <template v-if="titles" v-for="item in titles">
                <el-breadcrumb-item>{{item}}</el-breadcrumb-item>
            </template>
        </el-breadcrumb>
    </div>
</script>
<script>
    require.config({
        baseUrl: contextPath,
        // urlArgs: 'v=' + (window.APP_CONFIG.env === 'dev' ? new Date().getTime() : window.APP_CONFIG.version),
        waitSeconds: 0,
        paths: {
            vue: '/libs/vue/2.6.10/vue',
            ELEMENT: '/libs/element/2.4.5/index',
            text: 'https://cdn.bootcss.com/require-text/2.0.12/text.min',
            Lit: '/libs/lit-common',
            // vue: (window.APP_CONFIG.env === 'dev' ? 'assets/libs/vue-2.5.17/vue' : 'assets/libs/vue-2.5.17/vue.min'),
            'vue-i18n': 'assets/libs/vue-i18n-8.1.0/vue-i18n.min',
        }
    })
    require(['vue', 'ELEMENT', '/views/layout-admin.js', '${view}'], function (Vue, ELEMENT, app, component) {

        Vue.use(ELEMENT, {size: 'medium'})

        Vue.component('app-breadcrumb', {
            template: '#app-breadcrumb-template',
            props: {
                title: String,
                titles: Array
            }
        })
        Vue.component('router-view', {template: '<i></i>'})
        Vue.component('app-main', component)
        Vue.component('app', app)
        new Vue({el: '#app'});
    })

    function redirect(url) {
        window.location.href = contextPath + url
    }
</script>
</body>
</html>