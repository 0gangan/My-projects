<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMenu, ElMenuItem, ElContainer, ElHeader, ElAside, ElMain } from 'element-plus'

const router = useRouter()
const activeMenu = ref('/buyer')  // 默认选中

function handleSelect(key) {
  activeMenu.value = key
  router.push(key)
}
</script>

<template>
  <router-view />
</template>


<style scoped>
.el-menu-vertical-demo {
  border-right: none;
}
</style>
