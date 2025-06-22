<template>
  <el-card v-if="orderId" class="order-detail" style="margin-top: 20px">
    <h2>订单详情 - 订单ID: {{ order.id }}</h2>

    <el-skeleton :loading="loading" animated>
      <template #template>
        <el-skeleton-item variant="text" style="width: 50%" />
        <el-skeleton-item variant="text" style="width: 30%" />
        <el-skeleton-item variant="text" style="width: 40%" />
      </template>

      <template #default>
        <p><strong>买家用户名：</strong>{{ order.user?.username || '无' }}</p>
        <p><strong>商家名称：</strong>{{ order.merchant?.name || '无' }}</p>
        <p><strong>订单状态：</strong>{{ order.status }}</p>
        <p><strong>下单时间：</strong>{{ formatDate(order.orderTime) }}</p>
        <p><strong>总价：</strong>{{ order.totalAmount?.toFixed(2) || '0.00' }} 元</p>

        <h3>菜品详情</h3>
        <el-table :data="order.details" v-if="order.details && order.details.length > 0" style="width: 100%">
          <el-table-column prop="dish.name" label="菜品名称" />
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column
            prop="price"
            label="单价"
            width="100"
            :formatter="row => row.price.toFixed(2) + ' 元'"
          />
          <el-table-column
            label="小计"
            width="100"
            :formatter="row => (row.price * row.quantity).toFixed(2) + ' 元'"
          />
        </el-table>
        <div v-else>没有菜品信息</div>
      </template>
    </el-skeleton>
  </el-card>

  <div v-else style="margin-top: 20px;">
    <el-empty description="请选择一个订单查看详情" />
  </div>
</template>

<script>
import { ref, watch, onMounted } from 'vue'
import { getOrderDetail } from '@/api/orders.js'

export default {
  name: 'OrderDetail',
  props: {
    orderId: {
      type: [String, Number],
      required: false
    }
  },
  setup(props) {
    const order = ref({})
    const loading = ref(false)

    const fetchOrderDetail = async (id) => {
      if (!id) return
      loading.value = true
      try {
        const res = await getOrderDetail(id)
        order.value = res.data || {}
      } catch (error) {
        console.error('获取订单详情失败', error)
      } finally {
        loading.value = false
      }
    }

    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString()
    }

    watch(() => props.orderId, (newId) => {
      fetchOrderDetail(newId)
    })

    onMounted(() => {
      fetchOrderDetail(props.orderId)
    })

    return {
      order,
      loading,
      formatDate
    }
  }
}
</script>
