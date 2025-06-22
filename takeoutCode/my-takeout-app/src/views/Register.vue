<template>
  <div class="register-container">
    <h2>注册</h2>
    <form @submit.prevent="handleRegister">
      <div>
        <label>用户名</label>
        <input v-model="username" type="text" required />
      </div>
      <div>
        <label>密码</label>
        <input v-model="password" type="password" required />
      </div>
      <div>
        <label>确认密码</label>
        <input v-model="confirmPassword" type="password" required />
      </div>
      <div>
        <label>角色</label>
        <select v-model="role" required>
          <option value="" disabled>请选择角色</option>
          <option value="USER">买家</option>
          <option value="MERCHANT">商家</option>
        </select>
      </div>
      <button type="submit" :disabled="loading">{{ loading ? '注册中...' : '注册' }}</button>
    </form>
    <p>
      已有账号？
      <router-link to="/login">去登录</router-link>
    </p>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '@/api/auth.js'

const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const role = ref('')
const error = ref('')
const loading = ref(false)
const router = useRouter()

async function handleRegister() {
  error.value = ''
  if (password.value !== confirmPassword.value) {
    error.value = '两次密码输入不一致'
    return
  }
  loading.value = true
  try {
    await register({ username: username.value, password: password.value, role: role.value })
    alert('注册成功，请登录')
    router.push({ name: 'login' })
  } catch (e) {
    error.value = e.response?.data?.message || e.message || '注册失败'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  max-width: 320px;
  margin: 100px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f9f9f9;
}
input, select {
  width: 100%;
  margin: 6px 0 12px;
  padding: 8px;
  box-sizing: border-box;
}
button {
  width: 100%;
  padding: 10px;
  background-color: #42b983;
  color: white;
  border: none;
  cursor: pointer;
}
button:disabled {
  background-color: #a5d6a7;
  cursor: not-allowed;
}
.error {
  margin-top: 10px;
  color: red;
}
</style>
