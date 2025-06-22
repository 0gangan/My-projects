import axios from 'axios'

const instance = axios.create({
  baseURL: 'http://localhost:8080',  // 你后端地址
  timeout: 5000
})

// 请求拦截，自动带token
instance.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
}, error => Promise.reject(error))

// 响应拦截（可选）
instance.interceptors.response.use(response => response, error => {
  // 统一处理错误
  return Promise.reject(error)
})

export default instance
