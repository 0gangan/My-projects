<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}
</script>

<template>
  <div class="merchant-layout">
    <header class="header">
      <h1>商家端 - 我的App</h1>
      <button @click="logout">退出登录</button>
    </header>
    <div class="container">
      <aside class="sidebar">
        <router-link to="/merchant">仪表盘</router-link>
        <router-link to="/merchant/orders">订单管理</router-link>
        <router-link to="/merchant/dishes">菜品管理</router-link>
        <router-link to="/merchant/profile">个人资料</router-link>
      </aside>
      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>



<style scoped>
.merchant-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
}
.header {
  background-color: #67c23a;
  color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.container {
  display: flex;
  flex: 1;
  overflow: hidden;
}
.sidebar {
  width: 200px;
  background-color: #f0f9eb;
  padding: 1rem;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}
.sidebar a {
  padding: 0.5rem 1rem;
  margin-bottom: 0.5rem;
  color: #3eaf7c;
  text-decoration: none;
  border-radius: 4px;
}
.sidebar a.router-link-exact-active {
  font-weight: bold;
  background-color: #d3f9d8;
}
.content {
  flex: 1;
  padding: 1rem 2rem;
  overflow-y: auto;
  background-color: #f9fafc;
}
</style>
