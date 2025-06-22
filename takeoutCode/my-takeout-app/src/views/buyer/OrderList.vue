<template>
  <el-card>
    <h2>我的订单列表</h2>
    <el-table
      :data="orders"
      v-loading="loading"
      style="width: 100%; cursor: pointer"
      @row-click="selectOrder"
      highlight-current-row
    >
      <el-table-column prop="id" label="订单ID" width="120" />
      <el-table-column prop="status" label="订单状态" width="120" />
      <el-table-column
        prop="totalAmount"
        label="总价 (元)"
        width="120"
        :formatter="(row) => row.totalAmount.toFixed(2)"
      />
    </el-table>
  </el-card>
</template>

<script>
import { ref, onMounted } from 'vue'
import { getOrdersByUserId } from '@/api/orders.js'

export default {
  name: 'OrderList',
  emits: ['orderSelected'],
  setup(props, { emit }) {
    const orders = ref([])
    const loading = ref(false)
    const userId = localStorage.getItem('userId') || 1

    const fetchOrders = async () => {
      loading.value = true
      try {
        const res = await getOrdersByUserId(userId)
        orders.value = res.data || []
      } catch (error) {
        console.error('获取订单列表失败', error)
      } finally {
        loading.value = false
      }
    }

    const selectOrder = (row) => {
      emit('orderSelected', row.id)
    }

    onMounted(() => {
      fetchOrders()
    })

    return {
      orders,
      loading,
      selectOrder
    }
  }
}
</script>
