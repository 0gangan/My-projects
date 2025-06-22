<template>
  <div class="order-container">
    <h2>商家订单管理</h2>

    <div v-if="loading">加载中...</div>

    <div v-else>
      <!-- 订单列表 -->
      <div v-if="!showDetail">
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
                <button @click="handleViewDetails(order.id)">查看详情</button>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-else>暂无订单</div>
      </div>

      <!-- 订单详情 -->
      <div v-if="showDetail && currentOrder">
        <button @click="handleBack">返回列表</button>

        <h3>订单详情 - 订单ID: {{ currentOrder.id }}</h3>
        <p><strong>买家用户名：</strong>{{ currentOrder.user?.username || '无' }}</p>
        <p><strong>订单状态：</strong>{{ currentOrder.status }}</p>
        <p><strong>下单时间：</strong>{{ formatDate(currentOrder.orderTime) }}</p>
        <p><strong>总价：</strong>{{ currentOrder.totalAmount?.toFixed(2) || '0.00' }} 元</p>

        <h4>菜品详情</h4>
        <table border="1" cellspacing="0" cellpadding="5" v-if="currentOrder.details && currentOrder.details.length > 0">
          <thead>
            <tr>
              <th>菜品名称</th>
              <th>数量</th>
              <th>单价</th>
              <th>小计</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in currentOrder.details" :key="item.id">
              <td>{{ item.dish?.name || '未知菜品' }}</td>
              <td>{{ item.quantity }}</td>
              <td>{{ item.price.toFixed(2) }} 元</td>
              <td>{{ (item.quantity * item.price).toFixed(2) }} 元</td>
            </tr>
          </tbody>
        </table>
        <div v-else>没有菜品信息</div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { getOrdersByMerchantId, getOrderDetail } from '@/api/orders.js'

export default {
  name: 'Order',
  setup() {
    const orders = ref([])
    const loading = ref(false)
    const showDetail = ref(false)
    const currentOrder = ref(null)

    const merchantId = localStorage.getItem('merchantId') // 或者你从其它地方拿商家id

    const fetchOrders = async () => {
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

    const fetchOrderDetail = async (orderId) => {
      loading.value = true
      try {
        const res = await getOrderDetail(orderId)
        currentOrder.value = res.data || null
        showDetail.value = true
      } catch (error) {
        console.error('获取订单详情失败', error)
      } finally {
        loading.value = false
      }
    }

    const handleViewDetails = (orderId) => {
      fetchOrderDetail(orderId)
    }

    const handleBack = () => {
      showDetail.value = false
      currentOrder.value = null
    }

    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      return new Date(dateStr).toLocaleString()
    }

    onMounted(() => {
      fetchOrders()
    })

    return {
      orders,
      loading,
      showDetail,
      currentOrder,
      formatDate,
      handleViewDetails,
      handleBack,
    }
  }
}
</script>

<style scoped>
.order-container {
  padding: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th {
  background-color: #f2f2f2;
}

button {
  cursor: pointer;
}
</style>
