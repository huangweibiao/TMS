import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { public: true }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      },
      {
        path: 'base/customer',
        name: 'Customer',
        component: () => import('@/views/base/customer/index.vue'),
        meta: { title: '客户管理', icon: 'UserFilled' }
      },
      {
        path: 'base/vehicle',
        name: 'Vehicle',
        component: () => import('@/views/base/vehicle/index.vue'),
        meta: { title: '车辆管理', icon: 'Van' }
      },
      {
        path: 'base/driver',
        name: 'Driver',
        component: () => import('@/views/base/driver/index.vue'),
        meta: { title: '司机管理', icon: 'User' }
      },
      {
        path: 'base/warehouse',
        name: 'Warehouse',
        component: () => import('@/views/base/warehouse/index.vue'),
        meta: { title: '仓库管理', icon: 'OfficeBuilding' }
      },
      {
        path: 'order/waybill',
        name: 'Waybill',
        component: () => import('@/views/order/waybill/index.vue'),
        meta: { title: '运单管理', icon: 'Document' }
      },
      {
        path: 'dispatch',
        name: 'Dispatch',
        component: () => import('@/views/dispatch/index.vue'),
        meta: { title: '调度管理', icon: 'Guide' }
      },
      {
        path: 'tracking',
        name: 'Tracking',
        component: () => import('@/views/tracking/index.vue'),
        meta: { title: '在途监控', icon: 'MapLocation' }
      },
      {
        path: 'finance/cost',
        name: 'Cost',
        component: () => import('@/views/finance/cost.vue'),
        meta: { title: '费用管理', icon: 'Money' }
      },
      {
        path: 'finance/settlement',
        name: 'Settlement',
        component: () => import('@/views/finance/settlement.vue'),
        meta: { title: '结算管理', icon: 'Wallet' }
      },
      {
        path: 'system/user',
        name: 'User',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'UserFilled' }
      },
      {
        path: 'system/role',
        name: 'Role',
        component: () => import('@/views/system/role/index.vue'),
        meta: { title: '角色管理', icon: 'Key' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.public) {
    next()
    return
  }
  
  if (!userStore.isLoggedIn) {
    next('/login')
    return
  }
  
  next()
})

export default router
