<template>
  <div class="merchant-page">
    <h2>{{ merchant.name }}</h2>
    <p>{{ merchant.description }}</p>
    <p><small>{{ merchant.address }} | {{ merchant.phone }}</small></p>

    <h3>菜品列表</h3>
    <div class="dishes-list">
      <div 
        v-for="dish in dishes" 
        :key="dish.id" 
        class="dish-card"
      >
        <h4>{{ dish.name }}</h4>
        <p>{{ dish.description }}</p>
        <p class="price">¥{{ dish.price.toFixed(2) }}</p>
        <div class="quantity-controls">
          <button @click="decrement(dish)" :disabled="getQuantity(dish.id) === 0">-</button>
          <span>{{ getQuantity(dish.id) }}</span>
          <button @click="increment(dish)">+</button>
        </div>
      </div>
    </div>

    <div class="cart">
      <h3>购物车</h3>
      <div v-if="cartItems.length === 0" class="empty-cart">购物车为空</div>
      <ul v-else>
        <li v-for="item in cartItems" :key="item.id">
          {{ item.name }} × {{ item.quantity }} - ¥{{ (item.price * item.quantity).toFixed(2) }}
        </li>
      </ul>
      <div class="total">
        总价: ¥{{ totalPrice.toFixed(2) }}
      </div>
      <button 
        @click="submitOrder" 
        :disabled="cartItems.length === 0 || loading"
      >
        {{ loading ? '提交中...' : '提交订单' }}
      </button>
    </div>
  </div>
</template>

<!-- <script setup>
import { ref, onMounted, onBeforeUnmount, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'  // ✅ 引入 user store
import axios from '@/utils/apiAxios'

const route = useRoute()
const router = useRouter()

const userStore = useUserStore()  // ✅ 使用 pinia 获取用户信息

const merchant = ref({})
const dishes = ref([])
const cart = ref({})
const loading = ref(false)

const merchantId = route.params.id

const fetchMerchant = async () => {
  try {
    const res = await axios.get(`/merchants/${merchantId}`)
    merchant.value = res.data
  } catch (err) {
    console.error('获取商家信息失败', err)
  }
}

const fetchDishes = async () => {
  try {
    const res = await axios.get(`/merchants/${merchantId}/dishes`)
    dishes.value = res.data
  } catch (err) {
    console.error('获取菜品失败', err)
  }
}

onMounted(async () => {
  await fetchMerchant()
  await fetchDishes()
})

onBeforeUnmount(() => {
  cart.value = {}
})

const increment = (dish) => {
  if (!cart.value[dish.id]) cart.value[dish.id] = 0
  cart.value[dish.id]++
}

const decrement = (dish) => {
  if (cart.value[dish.id] > 0) {
    cart.value[dish.id]--
    if (cart.value[dish.id] === 0) {
      delete cart.value[dish.id]
    }
  }
}

const getQuantity = (dishId) => cart.value[dishId] || 0

const cartItems = computed(() => {
  return dishes.value
    .filter(dish => cart.value[dish.id])
    .map(dish => ({
      ...dish,
      quantity: cart.value[dish.id]
    }))
})

const totalPrice = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

const submitOrder = async () => {
  if (cartItems.value.length === 0) return
  loading.value = true
  try {
    const userId = userStore.id
    const address = userStore.address

    // ✅ 打印当前用户信息
    console.log('当前用户信息：', {
      id: userStore.id,
      username: userStore.username,
      role: userStore.role,
      email: userStore.email,
      phone: userStore.phone,
      address: userStore.address
    })

    // if (!userId || !address) {
    //   alert('用户信息不完整，无法提交订单')
    //   return
    // }
    if (!userId ) {
      alert('用户信息不完整，无法提交订单')
      return
    }
    const orderPayload = {
      merchantId: Number(merchantId),
      address: address,
      items: cartItems.value.map(i => ({
        dishId: i.id,
        quantity: i.quantity
      }))
    }

    await axios.post(`/orders/user/userId=${userId}`, orderPayload)

    alert('订单提交成功！')
    cart.value = {}
    router.push('/buyer')
  } catch (err) {
    alert('提交订单失败，请稍后重试')
    console.error(err)
  } finally {
    loading.value = false
  }
} -->
<!-- 
</script> -->
<script setup>
import { ref, onMounted, onBeforeUnmount, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import axios from '@/utils/apiAxios'

const route = useRoute()
const router = useRouter()

const userStore = useUserStore()

const merchant = ref({})
const dishes = ref([])
const cart = ref({})
const loading = ref(false)

const merchantId = route.params.id

// 从 localStorage 读取地址
const localAddress = ref('')

// 获取商家信息
const fetchMerchant = async () => {
  try {
    const res = await axios.get(`/merchants/${merchantId}`)
    merchant.value = res.data
  } catch (err) {
    console.error('获取商家信息失败', err)
  }
}

// 获取商家菜品列表
const fetchDishes = async () => {
  try {
    const res = await axios.get(`/merchants/${merchantId}/dishes`)
    dishes.value = res.data
  } catch (err) {
    console.error('获取菜品失败', err)
  }
}

onMounted(async () => {
  // 读取地址并打印
  localAddress.value = localStorage.getItem('address') || ''
  console.log('从localStorage读取的地址:', localAddress.value)

  await fetchMerchant()
  await fetchDishes()
})

onBeforeUnmount(() => {
  cart.value = {}
})

const increment = (dish) => {
  if (!cart.value[dish.id]) cart.value[dish.id] = 0
  cart.value[dish.id]++
}

const decrement = (dish) => {
  if (cart.value[dish.id] > 0) {
    cart.value[dish.id]--
    if (cart.value[dish.id] === 0) {
      delete cart.value[dish.id]
    }
  }
}

const getQuantity = (dishId) => cart.value[dishId] || 0

const cartItems = computed(() => {
  return dishes.value
    .filter(dish => cart.value[dish.id])
    .map(dish => ({
      ...dish,
      quantity: cart.value[dish.id]
    }))
})

const totalPrice = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

const submitOrder = async () => {
  if (cartItems.value.length === 0) {
    alert('购物车为空，无法提交订单')
    return
  }
  if (!userStore.id || !userStore.token) {
    alert('用户未登录或身份信息缺失，请先登录')
    router.push('/login')
    return
  }

  const addressToUse = localAddress.value || userStore.address
  if (!addressToUse) {
    alert('请先完善地址信息')
    return
  }

  loading.value = true
  try {
    const orderPayload = {
      order: {
        totalAmount: totalPrice.value,
        address: addressToUse,
        status: '待支付',
        user: { id: userStore.id },
        merchant: { id: Number(merchantId) }
      },
      details: cartItems.value.map(item => ({
        quantity: item.quantity,
        price: item.price,
        dish: { id: item.id }
      }))
    }

    console.log('发送的订单数据：', orderPayload)

    await axios.post('/orders', orderPayload)

    alert('订单提交成功！')
    cart.value = {}
    router.push('/buyer')
  } catch (err) {
    console.error('提交订单失败', err)
    alert('提交订单失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

</script>


<style scoped>
.merchant-page {
  padding: 20px;
}
.dishes-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-top: 20px;
}
.dish-card {
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 12px;
  width: 200px;
  box-shadow: 1px 1px 4px rgba(0,0,0,0.1);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.dish-card h4 {
  margin: 0 0 8px;
}
.price {
  font-weight: bold;
  margin: 8px 0;
}
.quantity-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}
.quantity-controls button {
  width: 28px;
  height: 28px;
  border: none;
  border-radius: 4px;
  background-color: #409EFF;
  color: white;
  cursor: pointer;
  font-size: 18px;
  line-height: 1;
  user-select: none;
  transition: background-color 0.3s;
}
.quantity-controls button:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}
.quantity-controls span {
  min-width: 24px;
  text-align: center;
}
.cart {
  margin-top: 40px;
  border-top: 2px solid #409EFF;
  padding-top: 20px;
}
.cart h3 {
  margin-bottom: 10px;
}
.empty-cart {
  color: #999;
  font-style: italic;
}
.cart ul {
  list-style: none;
  padding-left: 0;
  margin-bottom: 10px;
}
.cart li {
  padding: 6px 0;
  border-bottom: 1px solid #eee;
}
.total {
  font-weight: bold;
  font-size: 18px;
  margin-bottom: 12px;
  text-align: right;
}
button[disabled] {
  background-color: #a0cfff !important;
  cursor: not-allowed !important;
}
</style>
