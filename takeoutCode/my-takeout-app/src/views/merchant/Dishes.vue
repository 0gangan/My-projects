<template>
  <div class="dishes">
    <el-page-header content="菜品管理" class="header" />

    <el-card shadow="hover">
      <template #header>菜品列表</template>

      <el-table :data="dishes" style="width: 100%">
        <el-table-column label="菜品名称">
          <template #default="{ row }">
            <el-input v-model="row.name" placeholder="请输入名称" />
          </template>
        </el-table-column>

        <el-table-column label="价格">
          <template #default="{ row }">
            <el-input v-model.number="row.price" placeholder="请输入价格" type="number" />
          </template>
        </el-table-column>

        <el-table-column label="描述">
          <template #default="{ row }">
            <el-input v-model="row.description" placeholder="请输入描述" />
          </template>
        </el-table-column>

        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="saveDish(row)">保存</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="dishes.length === 0" description="暂无菜品" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import instance from '@/api/index.js'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const dishes = ref([])
const userStore = useUserStore()

async function fetchDishes(merchantId) {
  if (!merchantId) {
    ElMessage.error('无法获取商家信息，请重新登录')
    dishes.value = []
    return
  }

  try {
    const res = await instance.get(`/merchants/${merchantId}/dishes`)
    dishes.value = Array.isArray(res.data) ? res.data : []
  } catch (error) {
    ElMessage.error('获取菜品失败')
    dishes.value = []
  }
}

async function saveDish(dish) {
  try {
    const res = await instance.put(`/dishes/${dish.id}`, {
      name: dish.name,
      price: dish.price,
      description: dish.description
    })
    ElMessage.success('保存成功')
  } catch (err) {
    console.error('保存失败', err)
    ElMessage.error('保存失败，请稍后重试')
  }
}

onMounted(() => {
  fetchDishes(userStore.user?.merchantId)
})
</script>

<style scoped>
.dishes {
  padding: 20px;
}
.header {
  margin-bottom: 20px;
}
</style>
