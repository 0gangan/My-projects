import axios from './index.js'

// 获取所有菜品（可加分页、筛选参数）
export function getDishes() {
  return axios.get('/dishes')
}

// 根据商家ID获取该商家的菜品列表（改成后端正确路径）
export function getDishesByMerchantId(merchantId) {
  return axios.get(`/merchants/${merchantId}/dishes`)
}

// 根据菜品ID获取菜品详情
export function getDishById(dishId) {
  return axios.get(`/dishes/${dishId}`)
}

// 新增菜品（商家权限）
export function createDish(dishData) {
  return axios.post('/dishes', dishData)
}

// 更新菜品信息
export function updateDish(dishId, updateData) {
  return axios.put(`/dishes/${dishId}`, updateData)
}

// 删除菜品
export function deleteDish(dishId) {
  return axios.delete(`/dishes/${dishId}`)
}
