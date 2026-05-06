import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { UserInfo, LoginResponse } from '@/types'

export const useUserStore = defineStore('user', () => {
  // State
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfo | null>(null)

  // Getters
  const isLoggedIn = computed(() => !!token.value)
  const hasRole = (role: string) => {
    return userInfo.value?.roles?.includes(role) || false
  }

  // Actions
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  const login = (data: LoginResponse) => {
    setToken(data.token)
    setUserInfo({
      id: data.userId,
      username: data.username,
      realName: data.realName,
      phone: '',
      email: '',
      roles: data.roles
    })
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  const initUserInfo = () => {
    const storedUserInfo = localStorage.getItem('userInfo')
    if (storedUserInfo) {
      userInfo.value = JSON.parse(storedUserInfo)
    }
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    hasRole,
    setToken,
    setUserInfo,
    login,
    logout,
    initUserInfo
  }
})
