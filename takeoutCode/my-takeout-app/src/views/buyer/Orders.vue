<template>
  <div>
    <OrderList @orderSelected="orderId = $event" />
    <OrderDetail :orderId="orderId" />
  </div>
</template>

<script>
import { ref } from 'vue'
import OrderList from './OrderList.vue'
import OrderDetail from './OrderDetail.vue'

export default {
  components: { OrderList, OrderDetail },
  setup() {
    const orderId = ref(null)
    return { orderId }
  }
}
</script>
