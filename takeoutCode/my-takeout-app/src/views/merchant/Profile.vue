<template>
  <el-card class="merchant-profile" shadow="hover">
    <template #header>
      <h2>商家资料</h2>
    </template>

    <el-skeleton :loading="loading" animated>
      <div v-if="error">
        <el-alert
          title="错误"
          type="error"
          :description="error"
          show-icon
          closable
        />
      </div>
      <div v-else>
        <el-form label-position="top" :model="form" class="form" label-width="100px">
          <el-form-item label="用户名">
            <el-input v-model="form.user.username" />
          </el-form-item>

          <el-form-item label="邮箱">
            <el-input v-model="form.user.email" />
          </el-form-item>

          <el-form-item label="电话">
            <el-input v-model="form.user.phone" />
          </el-form-item>

          <el-form-item label="地址">
            <el-input v-model="form.user.address" />
          </el-form-item>

          <el-divider />

          <el-form-item label="商家名称">
            <el-input v-model="form.name" />
          </el-form-item>

          <el-form-item label="商家描述">
            <el-input type="textarea" v-model="form.description" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="saveChanges">保存修改</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-skeleton>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '@/api/index.js'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const merchant = ref(null)
const loading = ref(true)
const error = ref(null)
const form = ref({
  user: {
    username: '',
    email: '',
    phone: '',
    address: ''
  },
  name: '',
  description: ''
})

const userStore = useUserStore()

async function fetchMerchantProfile() {
  if (userStore.role !== 'MERCHANT') {
    error.value = '当前用户非商家，无法获取商家信息'
    return
  }

  loading.value = true
  error.value = null
  try {
    const res = await axios.get(`/users/${userStore.id}/merchant`)
    merchant.value = res.data
    form.value = JSON.parse(JSON.stringify(res.data)) // 深拷贝
  } catch (err) {
    console.error('获取商家信息失败:', err)
    error.value = '无法获取商家信息，请稍后重试。'
  } finally {
    loading.value = false
  }
}

async function saveChanges() {
  try {
    const res = await axios.put(`/merchants/${merchant.value.id}`, form.value)
    ElMessage.success('保存成功')
    merchant.value = res.data
  } catch (err) {
    console.error('保存失败:', err)
    ElMessage.error('保存失败，请稍后重试')
  }
}

onMounted(() => {
  fetchMerchantProfile()
})
</script>

<style scoped>
.merchant-profile {
  max-width: 600px;
  margin: 20px auto;
}
.form {
  display: flex;
  flex-direction: column;
}
</style>
