<template>
  <div class="dashboard">
    <el-page-header content="商家后台首页" icon="" class="header" />

    <div class="stats">
      <el-card class="box-card" shadow="hover">
        <template #header>今日订单</template>
        <div class="card-content">
          <el-icon><ShoppingCart /></el-icon>
          <span class="stat-number">{{ todayOrders }}</span>
        </div>
      </el-card>
    </div>

    <el-divider />

    <el-card shadow="hover">
      <template #header>商家信息</template>
      <p><strong>商家名称：</strong>{{ merchantInfo.name || '暂无' }}</p>
      <p><strong>地址：</strong>{{ merchantInfo.address || '暂无' }}</p>
      <p><strong>电话：</strong>{{ merchantInfo.phone || '暂无' }}</p>
      <p><strong>简介：</strong>{{ merchantInfo.description || '暂无' }}</p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import axios from '@/api/index.js'
import { ElMessage } from 'element-plus'
import { ShoppingCart } from '@element-plus/icons-vue'

const userStore = useUserStore()
const merchantId = userStore.user?.id

const todayOrders = ref(0)
const merchantInfo = ref({})

onMounted(async () => {
  if (!merchantId) {
    ElMessage.error('请先登录')
    return
  }

  try {
    const res = await axios.get(`/users/${merchantId}/merchant`)
    merchantInfo.value = res.data

    const orderRes = await axios.get(`/merchants/${merchantInfo.value.id}/orders/today-count`)
    todayOrders.value = orderRes.data || 0
  } catch (err) {
    ElMessage.error('加载数据失败，请稍后重试')
    console.error(err)
  }
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}
.header {
  margin-bottom: 20px;
}
.stats {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}
.card-content {
  display: flex;
  align-items: center;
  gap: 10px;
}
.stat-number {
  font-size: 24px;
  font-weight: bold;
}
</style>
