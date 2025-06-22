<template>
  <div class="buyer-home">
    <el-page-header :content="welcomeText" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '../../stores/user.js'

const userStore = useUserStore()
const user = userStore.user

const welcomeText = computed(() => {
  return user && user.username ? `欢迎，${user.username}！` : '欢迎，访客！'
})
</script>

<style scoped>
.buyer-home {
  padding: 20px;
  text-align: center;
}
</style>
