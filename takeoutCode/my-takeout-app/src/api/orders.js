import instance from './index.js'

// 根据用户 ID 获取订单列表
export function getOrdersByUserId(userId) {
  return instance.get(`/orders/user/${userId}`)
}

// 根据商家 ID 获取订单列表
export function getOrdersByMerchantId(merchantId) {
  return instance.get(`/orders/merchant/${merchantId}`)
}

// 提交订单
export function submitOrder(orderRequest) {
  return instance.post('/orders', orderRequest)
}

// 根据订单 ID 获取订单详情
export function getOrderDetail(orderId) {
  return instance.get(`/orders/${orderId}`)
}
