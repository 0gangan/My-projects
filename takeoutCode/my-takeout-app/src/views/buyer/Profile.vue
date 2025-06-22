<template>
  <div class="profile" style="padding: 20px; max-width: 600px;">
    <h2>我的资料</h2>
    <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" />
      </el-form-item>

      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" />
      </el-form-item>

      <el-form-item label="地址" prop="address">
        <el-input v-model="form.address" />
      </el-form-item>

      <el-form-item label="角色">
        <el-input v-model="form.role" disabled />
      </el-form-item>

      <el-form-item label="注册时间">
        <el-input :value="formatDate(form.createdAt)" disabled />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm">保存修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import apiAxios from '@/utils/apiAxios'
import { useUserStore } from '@/stores/user'

const form = ref({
  username: '',
  phone: '',
  email: '',
  address: '',   // 新增地址字段
  role: '',
  createdAt: ''
})

const formRef = ref(null)
const userStore = useUserStore()

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式错误', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式错误', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入地址', trigger: 'blur' }
  ]
}

const syncUserToForm = () => {
  form.value.username = userStore.username
  form.value.role = userStore.role
  form.value.email = userStore.email
  form.value.phone = userStore.phone
  form.value.address = userStore.address || ''   // 同步地址
}

syncUserToForm()

onMounted(async () => {
  try {
    // 用 userStore.id 调用正确接口
    const res = await apiAxios.get(`/users/${userStore.id}`)
    Object.assign(form.value, res.data)
  } catch (e) {
    ElMessage.error('获取用户信息失败')
  }
})


watch(() => [userStore.username, userStore.role, userStore.email, userStore.phone, userStore.address], () => {
  syncUserToForm()
})

const submitForm = () => {
  formRef.value.validate(async valid => {
    if (valid) {
      try {
        await apiAxios.put(`/users/${form.value.id}`, {
          username: form.value.username,
          phone: form.value.phone,
          email: form.value.email,
          address: form.value.address   // 提交地址
        })
        ElMessage.success('修改成功')
        userStore.setUser({
          id: form.value.id,
          username: form.value.username,
          role: form.value.role,
          email: form.value.email,
          phone: form.value.phone,
          address: form.value.address    // 更新地址
        })
      } catch (e) {
        ElMessage.error('修改失败')
      }
    }
  })
}

const formatDate = (datetimeStr) => {
  if (!datetimeStr) return ''
  return new Date(datetimeStr).toLocaleString()
}
</script>

<style scoped>
h2 {
  color: #409EFF;
  margin-bottom: 20px;
}
</style>
