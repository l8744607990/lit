define(['Lit'], function (Lit) {
    let tmpl = `
<div v-cloak ref="auiWrapper" class="aui-wrapper" v-loading.fullscreen.lock="loading" element-loading-text="拼命加载中"
     :class="['aui-header--' + headerSkin,'aui-aside--' + asideSkin,
        {
          'aui-header--fixed': headerFixed,
          'aui-aside--fixed': asideFixed,
          'aui-aside--fold': asideFold,
          'aui-aside--top': asideTop,
          'aui-control--open': controlOpen,
          'aui-control--fixed': controlFixed
        }]">

    <template v-if="!loading">
        <!-- aui-header -->
        <header class="aui-header">
            <div class="aui-header__hd">
                <a class="aui-brand aui-brand--lg" href="#">Lit-Admin</a>
                <a class="aui-brand aui-brand--sm" href="#">Lit</a>
            </div>
            <div class="aui-header__bd">
                <!-- aui-header__menu left -->
                <el-menu class="aui-header__menu aui-header__menu--left" mode="horizontal">
                    <el-menu-item v-if="!asideTop" index="1" @click="asideFold = !asideFold">
                        <i class="ic icoutdent aui-header__icon-menu aui-header__icon-menu--rz180" aria-hidden="true"></i>
                    </el-menu-item>
                </el-menu>
                <!-- aui-header__menu right -->
                <el-menu class="aui-header__menu aui-header__menu--right" mode="horizontal">
                    <el-submenu index="3" :popper-append-to-body="false">
                        <template slot="title">
                            <img class="aui-avatar" src="/images/avatar.png" alt="">
                            <span>admin</span>
                        </template>
                        <el-menu-item index="3-1">修改密码</el-menu-item>
                        <el-menu-item index="3-2">退出</el-menu-item>
                    </el-submenu>
                    <el-menu-item index="1" @click="controlOpen = !controlOpen">
                        <i class="el-icon-setting aui-header__icon-menu"></i>
                    </el-menu-item>
                </el-menu>
            </div>
        </header>

        <!-- aside menu -->
        <aside class="aui-aside">
            <div class="aui-aside__inner">
                <el-menu v-if="asideMenuVisible"
                         class="aui-aside__menu"
                         :collapse="asideFold"
                         :default-active="activeMenuIndex"
                         :collapse-transition="false"
                         :mode="asideTop ? 'horizontal' : 'vertical'"
                         @select="select">
                    <el-menu-item index="home">
                        <i class="ic ichome"></i>
                        <span slot="title">首页</span>
                    </el-menu-item>

                    <template v-for="(item, index) in menuList">
                        <el-submenu v-if="item.isParent" :index="String(item.id)" :popper-append-to-body="false">
                            <template slot="title">
                                <i :class="item.icon"></i>
                                <span slot="title">{{item.name}}</span>
                            </template>
                            <template v-for="(subItem, subIndex) in item.children">
                                <el-menu-item :index="String(subItem.id)">
                                    <i :class="subItem.icon"></i>
                                    <span slot="title">{{subItem.name}}</span>
                                </el-menu-item>
                            </template>
                        </el-submenu>
                        <el-menu-item v-else :index="String(item.id)">
                            <i :class="item.icon"></i>
                            <span slot="title">{{item.name}}</span>
                        </el-menu-item>
                    </template>
                </el-menu>
            </div>
        </aside>

        <!-- aui-control -->
        <aside class="aui-control">
            <div class="aui-control__inner">
                <div class="aui-control__bd">
                    <el-tabs class="aui-tabs aui-tabs--flex" v-model="controlTabsActive">
                        <el-tab-pane label="Layout" name="layout">
                            <dl class="aui-control__setting">
                                <dt>Header</dt>
                                <dd><el-checkbox v-model="headerFixed">Fixed 固定</el-checkbox></dd>
                                <dd><el-checkbox v-model="headerSkin" true-label="colorful" false-label="white">Colorful 鲜艳</el-checkbox></dd>
                            </dl>
                            <dl class="aui-control__setting">
                                <dt>Aside</dt>
                                <dd><el-checkbox v-model="asideFixed">Fixed 固定</el-checkbox></dd>
                                <dd><el-checkbox v-model="asideSkin" true-label="dark" false-label="white">Dark 鲜艳</el-checkbox></dd>
                                <dd><el-checkbox v-model="asideTop">Top 至头部</el-checkbox></dd>
                            </dl>
                            <dl class="aui-control__setting">
                                <dt>Control</dt>
                                <dd><el-checkbox v-model="controlFixed">Fixed 固定</el-checkbox></dd>
                            </dl>
                        </el-tab-pane>
                        <el-tab-pane label="Skins" name="skins">
                            <dl class="aui-control__setting">
                                <dt>Skins</dt>
                                <dd v-for="item in skinList" :key="item.name">
                                    <el-radio v-model="skin" :label="item.name" @change="handleSkinChange">
                                        <span class="t-capitalize">{{ item.name }}</span> {{ item.remark }}
                                    </el-radio>
                                </dd>
                            </dl>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </div>
        </aside>

        <!-- main -->
        <app-main></app-main>
        <router-view/>
    </template>
</div>
    `
    return {
        template: tmpl,
        data: function () {
            return {
                loading: true,
                menuList: [],
                headerSkin: 'colorful', // 头部, 皮肤 (white 白色 / colorful 鲜艳)
                headerFixed: false, // 头部, 固定状态
                asideSkin: 'white', // 侧边, 皮肤 (white 白色 / dark 黑色)
                asideFixed: false, // 侧边, 固定状态
                asideFold: sessionStorage.getItem('asideFold') === 'true', // 侧边, 折叠状态
                asideTop: false, // 侧边, 至头部状态
                controlFixed: false,
                asideMenuVisible: true, // 侧边, 菜单显示状态 (控制台“至头部”操作时, el-menu组件需根据mode属性重新渲染)
                activeMenuIndex: sessionStorage.getItem('activeMenuIndex') || 'home',
                controlTabsActive: 'layout',
                controlOpen: false,
                skin: 'cyan',  // 皮肤, 默认值
                // 皮肤, 列表
                skinList: [
                    {name: 'blue', color: '#3E8EF7', remark: '蓝色'},
                    {name: 'brown', color: '#997B71', remark: '棕色'},
                    {name: 'cyan', color: '#0BB2D4', remark: '青色'},
                    {name: 'gray', color: '#757575', remark: '灰色'},
                    {name: 'green', color: '#11C26D', remark: '绿色'},
                    {name: 'indigo', color: '#667AFA', remark: '靛青色'},
                    {name: 'orange', color: '#EB6709', remark: '橙色'},
                    {name: 'pink', color: '#F74584', remark: '粉红色'},
                    {name: 'purple', color: '#9463F7', remark: '紫色'},
                    {name: 'red', color: '#FF4C52', remark: '红色'},
                    {name: 'turquoise', color: '#17B3A3', remark: '蓝绿色'},
                    {name: 'yellow', color: '#FCB900', remark: '黄色'}
                ]
            }
        },
        created() {
            let configure = this.getLayoutConfigure()
            this.skin = configure.skin || 'cyan'
            if (this.skin !== 'cyan') {
                this.initSkin(this.skin)
            }
            this.headerSkin = configure.headerSkin || 'colorful'
            this.asideSkin = configure.asideSkin || 'white'
            this.headerFixed = configure.headerFixed || false
            this.asideFixed = configure.asideFixed || false
            this.asideTop = configure.asideTop || false
            this.controlFixed = configure.controlFixed || false
            this.initMenu()
            this.loading = false;
        },
        watch: {
            headerSkin(value) {
                this.setLayoutConfigure('headerSkin', value)
            },
            asideSkin(value) {
                this.setLayoutConfigure('asideSkin', value)
            },
            headerFixed(value) {
                this.setLayoutConfigure('headerFixed', value)
            },
            asideFixed(value) {
                this.setLayoutConfigure('asideFixed', value)
            },
            asideTop(value) {
                this.setLayoutConfigure('asideTop', value)
            },
            controlFixed(value) {
                this.setLayoutConfigure('controlFixed', value)
            },
            asideFold(value) {
                sessionStorage.setItem('asideFold', value)
            }
        },
        methods: {
            getLayoutConfigure() {
                return JSON.parse(localStorage.getItem('layoutConfigure')) || {}
            },
            setLayoutConfigure(key, value) {
                let configure = this.getLayoutConfigure()
                configure[key] = value
                localStorage.setItem('layoutConfigure', JSON.stringify(configure))
            },
            handleSkinChange(val) {
                this.initSkin(val)
                this.setLayoutConfigure('skin', val)
            },
            initSkin(val) {
                let styleList = [
                    {
                        id: 'J_elementTheme',
                        url: contextPath + '/libs/element/2.4.5/themes/' + val + '/index.css?t=' + new Date().getTime()
                    }, {
                        id: 'J_auiSKin',
                        url: contextPath + '/styles/themes/aui-' + val + '.css?t=' + new Date().getTime()
                    }
                ];
                for (var i = 0; i < styleList.length; i++) {
                    var el = document.querySelector('#' + styleList[i].id);
                    if (el) {
                        el.href = styleList[i].url;
                        continue;
                    }
                    el = document.createElement('link');
                    el.id = styleList[i].id;
                    el.href = styleList[i].url;
                    el.rel = 'stylesheet';
                    document.querySelector('head').appendChild(el);
                }
            },
            initMenu() {
                let myMenu = sessionStorage.getItem('myMenu');
                if (myMenu) {
                    this.menuList = JSON.parse(myMenu)
                } else {
                    Lit.httpRequest.get('/api/my/menu').then(res => {
                        if (res.success) {
                            this.menuList = res.result || []
                            sessionStorage.setItem('myMenu', JSON.stringify(this.menuList))
                        }
                    })
                }
            },
            select(index, indexPath) {
                sessionStorage.setItem('activeMenuIndex', index)
                if (index === 'home') {
                    redirect('/')
                    return
                }
                this.menuList.forEach(menu => {
                    if (String(menu.id) === index) {
                        redirect(menu.url)
                    } else if (menu.isParent) {
                        menu.children.forEach(subMenu => {
                            if (String(subMenu.id) === index) {
                                redirect(subMenu.url)
                            }
                        })
                    }
                })
            }
        }
    }
})