// src/utils/apiAxios.js
import axios from 'axios'

const apiAxios = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 5000
})

apiAxios.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default apiAxios
