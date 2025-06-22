<template>
  <div class="merchants">
    <h2>商家列表</h2>
    <ul>
      <li v-for="merchant in merchants" :key="merchant.id" @click="goToMerchant(merchant.id)">
        {{ merchant.name }} - {{ merchant.address }}
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMerchants } from '@/api/merchants.js'  // 你自己封装的接口

const router = useRouter()
const merchants = ref([])

async function fetchMerchants() {
  try {
    const res = await getMerchants()
    merchants.value = res.data || []
    console.log('获取到的商家数据:', res.data)
  } catch (err) {
    console.error('获取商家列表失败', err)
  }
}

function goToMerchant(id) {
  router.push({ name: 'merchantDetail', params: { id } })
}

onMounted(fetchMerchants)
</script>

<style scoped>
.merchants ul {
  list-style: none;
  padding: 0;
}

.merchants li {
  padding: 10px;
  border-bottom: 1px solid #ccc;
  cursor: pointer;
}

.merchants li:hover {
  background-color: #f5f5f5;
}
</style>
