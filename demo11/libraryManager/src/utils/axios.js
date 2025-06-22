import axios from 'axios'

// 创建 axios 实例
const service = axios.create({
  baseURL: '/api', // 会被 Vite 的代理拦截转发到 http://localhost:8080
  timeout: 5000,
})

// 请求拦截器：请求前自动带上 token
service.interceptors.request.use(
  (config) => {
    const token = sessionStorage.getItem('token')
    if (token) {
      config.headers['token'] = token // 这里是自定义的 header 名，后端读的是 token
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器：统一处理响应
service.interceptors.response.use(
  (response) => {
    return response.data // 默认只返回 data 部分
  },
  (error) => {
    console.error('请求出错:', error)
    return Promise.reject(error)
  }
)

export default service
