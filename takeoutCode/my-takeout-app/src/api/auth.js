import axios from './index'  // 你自己axios基础配置文件

export function login(data) {
  return axios.post('/auth/login', data).then(res => res.data)
}
export function register(data) {
  return axios.post('/auth/register', data).then(res => res.data)
}