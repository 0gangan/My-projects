<template>
  <div class="merchant-orders">
    <h2>商家订单管理</h2>
    <div v-if="loading">加载中...</div>
    <div v-else>
      <table v-if="orders.length > 0" border="1" cellspacing="0" cellpadding="5">
        <thead>
          <tr>
            <th>订单ID</th>
            <th>买家用户名</th>
            <th>订单状态</th>
            <th>总价</th>
            <th>下单时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.id">
            <td>{{ order.id }}</td>
            <td>{{ order.user?.username || '无' }}</td>
            <td>{{ order.status }}</td>
            <td>{{ order.totalAmount?.toFixed(2) || '0.00' }}</td>
            <td>{{ formatDate(order.orderTime) }}</td>
            <td>
              <button @click="goToDetail(order.id)">查看详情</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else>暂无订单</div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getOrdersByMerchantId } from '@/api/orders.js'

export default {
  name: 'MerchantOrders',
  setup() {
    const orders = ref([])
    const loading = ref(false)
    const router = useRouter()

    const merchantId = localStorage.getItem('merchantId') // 或从状态管理中取

    const fetchOrders = async () => {
      if (!merchantId) {
        console.error('未找到商家ID，无法获取订单')
        return
      }
      loading.value = true
      try {
        const res = await getOrdersByMerchantId(merchantId)
        orders.value = res.data || []
      } catch (error) {
        console.error('获取商家订单失败', error)
      } finally {
        loading.value = false
      }
    }

    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      return new Date(dateStr).toLocaleString()
    }

    const goToDetail = (orderId) => {
      router.push({ name: 'MerchantOrderDetail', params: { id: orderId } })
    }

    onMounted(() => {
      fetchOrders()
    })

    return { orders, loading, formatDate, goToDetail }
  }
}
</script>

<style scoped>
.merchant-orders {
  padding: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th {
  background-color: #f2f2f2;
}
</style>
