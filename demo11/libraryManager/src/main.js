import './assets/main.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'     //导入路由
import api from './utils/api.js'  //导入axios封装
import ElementPlus from 'element-plus'  //导入element-plus
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
app.config.globalProperties.$api = api      //全局对象
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(router).use(ElementPlus).mount('#app')
