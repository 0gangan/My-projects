<template>
  <div class="merchant-order-detail">
    <h2>订单详情 - 订单ID: {{ order.id }}</h2>
    <div v-if="loading">加载中...</div>
    <div v-else-if="order.id">
      <p><strong>买家用户名：</strong>{{ order.user?.username || '无' }}</p>
      <p><strong>订单状态：</strong>{{ order.status }}</p>
      <p><strong>下单时间：</strong>{{ formatDate(order.orderTime) }}</p>
      <p><strong>总价：</strong>{{ order.totalAmount?.toFixed(2) || '0.00' }} 元</p>

      <h3>菜品详情</h3>
      <table border="1" cellspacing="0" cellpadding="5" v-if="order.details && order.details.length > 0">
        <thead>
          <tr>
            <th>菜品名称</th>
            <th>数量</th>
            <th>单价</th>
            <th>小计</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in order.details" :key="item.id">
            <td>{{ item.dish?.name || '未知菜品' }}</td>
            <td>{{ item.quantity }}</td>
            <td>{{ item.price.toFixed(2) }} 元</td>
            <td>{{ (item.quantity * item.price).toFixed(2) }} 元</td>
          </tr>
        </tbody>
      </table>
      <div v-else>没有菜品信息</div>
    </div>
    <div v-else>订单不存在或无法获取订单信息。</div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getOrderDetail } from '@/api/orders.js'

export default {
  name: 'MerchantOrderDetail',
  setup() {
    const route = useRoute()
    const orderId = route.params.id

    const order = ref({})
    const loading = ref(false)

    const fetchOrderDetail = async () => {
      loading.value = true
      try {
        const res = await getOrderDetail(orderId)
        order.value = res.data || {}
      } catch (error) {
        console.error('获取订单详情失败', error)
      } finally {
        loading.value = false
      }
    }

    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      return new Date(dateStr).toLocaleString()
    }

    onMounted(() => {
      fetchOrderDetail()
    })

    return { order, loading, formatDate }
  }
}
</script>

<style scoped>
.merchant-order-detail {
  padding: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th {
  background-color: #f9f9f9;
}
</style>
