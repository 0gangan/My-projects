<template>
  <el-container style="height: 100vh;">
    <!-- 左侧菜单 -->
    <el-aside width="200px" style="background-color: #f5f5f5;">
      <el-menu
        default-active="orders"
        class="el-menu-vertical-demo"
        @select="handleMenuSelect"
      >
        <el-menu-item index="orders">查看订单</el-menu-item>
        <el-menu-item index="dishes">管理菜品</el-menu-item>
        <el-menu-item index="profile">商家信息</el-menu-item>
        <el-menu-item index="logout">退出登录</el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧内容区域 -->
    <el-container>
      <el-header style="background-color: #ffffff; text-align: center; font-size: 20px;">
        欢迎，商家！
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

function handleMenuSelect(index) {
  switch (index) {
    case 'orders':
      router.push('/merchants/orders')
      break
    case 'dishes':
      router.push('/merchants/dishes')
      break
    case 'profile':
      router.push('/merchants/profile')
      break
    case 'logout':
      // 清理 token 等操作
      localStorage.removeItem('token') // 或 sessionStorage.clear()
      router.push('/login')
      break
  }
}
</script>

<style scoped>
.el-menu-vertical-demo {
  border-right: none;
}
</style>
