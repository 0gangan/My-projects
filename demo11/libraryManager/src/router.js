import { createRouter, createWebHistory } from 'vue-router'
 //导入组件
import Login from './views/Login.vue'       //登录视图文件
import Register from './views/Register.vue'  //注册视图文件
import MainPage from './views/MainPage.vue'  //主页面视图
//建立路由路径配置
const routes = [  
    //登录路由路径      
    { path: '/login', component: Login },
    //注册路由路径
    { path: '/register', component: Register },
    //配置默认的路径，默认显示主页
    { path: '/', component: MainPage },
 ]
//建立路由对象
const router = createRouter({  
    history: createWebHistory(),
    routes, // 'routes: routes' 的缩写
})
export default router    //导出路由对象