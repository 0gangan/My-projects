<template>
  <div class="login-container">
    <h2>登录</h2>
    <form @submit.prevent="handleLogin">
      <div>
        <label>用户名</label>
        <input v-model="username" type="text" required />
      </div>
      <div>
        <label>密码</label>
        <input v-model="password" type="password" required />
      </div>
      <button type="submit" :disabled="loading">{{ loading ? '登录中...' : '登录' }}</button>
    </form>
    <p>
      没有账号？
      <router-link to="//register">去注册</router-link>
    </p>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script setup>
import { login } from '@/api/auth.js'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import axios from '@/api/index.js'  // 默认导入你的axios实例

const username = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)
const router = useRouter()
const userStore = useUserStore()

// async function handleLogin() {
//   error.value = ''
//   loading.value = true

//   // 登录前先清空之前的用户信息，避免残留
//   userStore.clearUser()

//   try {
//     const res = await login({ username: username.value, password: password.value })
//     console.log('登录返回:', res)

//     const baseUser = {
//       token: res.token,
//       role: res.role,
//       username: username.value,
//       id: res.id,
//       email: res.email,
//       phone: res.phone,
//       merchantId: '' // 默认空，后面如果是商家再赋值
//     }

//     // 先存token，保证axios拦截器能从localStorage取到token
//     userStore.setUser(baseUser)

//     if (res.role === 'MERCHANT') {
//       try {
//         const merchantRes = await axios.get(`/users/${res.id}/merchant`)
//         console.log('商家信息:', merchantRes.data)
//         baseUser.merchantId = merchantRes.data.id
//         // 更新store中的merchantId
//         userStore.setUser(baseUser)
//       } catch (err) {
//         console.error('获取商家信息失败:', err)
//         error.value = '商家信息获取失败，请联系管理员'
//         loading.value = false
//         return
//       }
//     }

//     if (res.role === 'BUYER') {
//       await router.push({ name: 'buyerHome' })
//     } else if (res.role === 'MERCHANT') {
//       await router.push({ name: 'merchantDashboard' })
//     } else {
//       error.value = '未知用户角色: ' + res.role
//     }
//   } catch (e) {
//     error.value = e.response?.data?.message || e.message || '登录失败'
//   } finally {
//     loading.value = false
//   }
// }
async function handleLogin() {
  error.value = ''
  loading.value = true

  // 登录前先清空之前的用户信息，避免残留
  userStore.clearUser()

  try {
    const res = await login({ username: username.value, password: password.value })
    console.log('登录返回:', res)

    const baseUser = {
      token: res.token,
      role: res.role,
      username: username.value,
      id: res.id,
      email: res.email,
      phone: res.phone,
      address: res.address || '',  // ✅ 加上地址字段
      merchantId: '' // 默认空，后面如果是商家再赋值
    }

    // 先存token和基本信息，保证axios拦截器能从 localStorage 取 token
    userStore.setUser(baseUser)

    // 如果是商家用户，额外获取商家信息
    if (res.role === 'MERCHANT') {
      try {
        const merchantRes = await axios.get(`/users/${res.id}/merchant`)
        console.log('商家信息:', merchantRes.data)
        baseUser.merchantId = merchantRes.data.id
        // 更新store中的merchantId
        userStore.setUser(baseUser)
      } catch (err) {
        console.error('获取商家信息失败:', err)
        error.value = '商家信息获取失败，请联系管理员'
        loading.value = false
        return
      }
    }

    // 跳转到相应页面
    if (res.role === 'BUYER') {
      await router.push({ name: 'buyerHome' })
    } else if (res.role === 'MERCHANT') {
      await router.push({ name: 'merchantDashboard' })
    } else {
      error.value = '未知用户角色: ' + res.role
    }
  } catch (e) {
    error.value = e.response?.data?.message || e.message || '登录失败'
  } finally {
    loading.value = false
  }
}


</script>


<style scoped>
.login-container {
  max-width: 320px;
  margin: 100px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f9f9f9;
}
input {
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
