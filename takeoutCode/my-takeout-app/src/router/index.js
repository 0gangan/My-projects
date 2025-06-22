import { createRouter, createWebHistory } from 'vue-router'

// 布局组件
import BlankLayout from '../layouts/BlankLayout.vue'
import BasicLayout from '../layouts/BasicLayout.vue'
import MerchantLayout from '../layouts/MerchantLayout.vue'

// 登录注册
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'

// 买家页面
import BuyerHome from '../views/buyer/BuyerHome.vue'
import Merchants from '../views/buyer/Merchants.vue'
import Merchant from '../views/buyer/Merchant.vue'
import Orders from '../views/buyer/Orders.vue'
import BuyerProfile from '../views/buyer/Profile.vue'

// 商家页面
import MerchantDashboard from '../views/merchant/Dashboard.vue'
import MerchantOrders from '../views/merchant/Orders.vue'
import MerchantDishes from '../views/merchant/Dishes.vue'
import MerchantProfile from '../views/merchant/Profile.vue'

// 404 页面
import NotFound from '../views/NotFound.vue'


const routes = [
  {
    path: '/login',
    component: BlankLayout,
    children: [{ path: '', name: 'login', component: Login }]
  },
  {
    path: '/register',
    component: BlankLayout,
    children: [{ path: '', name: 'register', component: Register }]
  },
  {
    path: '/buyer',
    component: BasicLayout,
    children: [
      { path: '', name: 'buyerHome', component: BuyerHome },
      { path: 'merchants', name: 'merchants', component: Merchants },
      { path: 'merchants/:id', name: 'merchantDetail', component: Merchant },
      { path: 'orders', name: 'buyerOrders', component: Orders },
      { path: 'profile', name: 'buyerProfile', component: BuyerProfile }
    ]
  },
{
  path: '/merchant',
  component: MerchantLayout,
  children: [
    { path: '', name: 'merchantDashboard', component: MerchantDashboard },
    { path: 'orders', name: 'merchantOrders', component: MerchantOrders },
    { path: 'dishes', name: 'merchantDishes', component: MerchantDishes },
    { path: 'profile', name: 'merchantProfile', component: MerchantProfile }
  ]
}


,


  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFound
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})



export default router
