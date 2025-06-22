<template>
  <el-container style="height: 100vh;">
    <el-aside width="200px" style="background-color: #fff;">
      <el-menu
        :default-active="activeMenu"
        @select="handleMenuSelect"
        router
        :unique-opened="true"
        class="el-menu-vertical-demo"
      >
        <el-menu-item index="/buyer">首页</el-menu-item>
        <el-menu-item index="/buyer/merchants">浏览商家</el-menu-item>
        <el-menu-item index="/buyer/orders">我的订单</el-menu-item>
        <el-menu-item index="/buyer/profile">个人资料</el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header style="background-color: #409EFF; color: white; text-align: center;">
        买家端 - 我的App
      </el-header>
      <el-main style="padding: 20px; overflow-y: auto;">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

// 绑定激活的菜单项，和路由保持同步
const activeMenu = ref(route.path)

watch(
  () => route.path,
  (newPath) => {
    activeMenu.value = newPath
  }
)

function handleMenuSelect(index) {
  router.push(index)
}
</script>

<style scoped>
.el-menu-vertical-demo {
  border-right: none;
}
</style>
