import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const role = ref(localStorage.getItem('role') || '')
  const username = ref(localStorage.getItem('username') || '')
  const id = ref(localStorage.getItem('id') || '')
  const merchantId = ref(localStorage.getItem('merchantId') || '')
  
  // 新增这些字段
  const phone = ref(localStorage.getItem('phone') || '')
  const email = ref(localStorage.getItem('email') || '')
  const address = ref(localStorage.getItem('address') || '')

  const user = computed(() => ({
    token: token.value,
    role: role.value,
    username: username.value,
    id: id.value,
    merchantId: merchantId.value,
    phone: phone.value,
    email: email.value,
    address: address.value
  }))

  function setUser({ token: t, role: r, username: u, id: i, merchantId: mId, phone: p, email: e, address: a }) {
    token.value = t
    role.value = r
    username.value = u
    id.value = i
    merchantId.value = mId || ''
    phone.value = p || ''
    email.value = e || ''
    address.value = a || ''

    localStorage.setItem('token', t)
    localStorage.setItem('role', r)
    localStorage.setItem('username', u)
    localStorage.setItem('id', i)
    if (mId) {
      localStorage.setItem('merchantId', mId)
    } else {
      localStorage.removeItem('merchantId')
    }
    if (p) {
      localStorage.setItem('phone', p)
    } else {
      localStorage.removeItem('phone')
    }
    if (e) {
      localStorage.setItem('email', e)
    } else {
      localStorage.removeItem('email')
    }
    if (a) {
      localStorage.setItem('address', a)
    } else {
      localStorage.removeItem('address')
    }
  }

  function clearUser() {
    token.value = ''
    role.value = ''
    username.value = ''
    id.value = ''
    merchantId.value = ''
    phone.value = ''
    email.value = ''
    address.value = ''

    localStorage.removeItem('token')
    localStorage.removeItem('role')
    localStorage.removeItem('username')
    localStorage.removeItem('id')
    localStorage.removeItem('merchantId')
    localStorage.removeItem('phone')
    localStorage.removeItem('email')
    localStorage.removeItem('address')
  }

  return { token, role, username, id, merchantId, phone, email, address, user, setUser, clearUser }
})
