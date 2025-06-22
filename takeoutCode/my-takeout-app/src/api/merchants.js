import axios from './index.js'  // 你基础axios配置
import instance from './index.js'
// 获取商家列表
export function getMerchants() {
  return instance.get('/merchants')
}

// 根据用户ID获取商家详情
export function getMerchantByUserId(userId) {
  return instance.get(`/user/${userId}/merchant`)
}

// 你还可以根据需求继续扩展，比如新增商家、更新商家等接口
// export function createMerchant(data) { return axios.post('/merchants', data) }
// export function updateMerchant(id, data) { return axios.put(`/merchants/${id}`, data) }
// export function deleteMerchant(id) { return axios.delete(`/merchants/${id}`) }
