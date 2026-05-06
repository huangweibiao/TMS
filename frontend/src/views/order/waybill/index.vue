<template>
  <div class="waybill-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>运单管理</span>
          <el-button type="primary" @click="handleAdd">创建运单</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="运单号">
          <el-input v-model="searchForm.waybillNo" placeholder="请输入运单号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待调度" :value="1" />
            <el-option label="已调度" :value="2" />
            <el-option label="提货中" :value="3" />
            <el-option label="运输中" :value="4" />
            <el-option label="已签收" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="waybillNo" label="运单号" width="150" />
        <el-table-column prop="customerName" label="客户" width="120" />
        <el-table-column prop="shipperAddress" label="发货地址" show-overflow-tooltip />
        <el-table-column prop="consigneeAddress" label="收货地址" show-overflow-tooltip />
        <el-table-column prop="totalWeight" label="重量(kg)" width="100" />
        <el-table-column prop="totalQuantity" label="件数" width="80" />
        <el-table-column prop="waybillStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.waybillStatus)">{{ getStatusText(row.waybillStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="primary" link @click="handleDispatch(row)" v-if="row.waybillStatus === 1">调度</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import type { Waybill } from '@/types'

const loading = ref(false)
const tableData = ref ref<Waybill[]>([])

const searchForm = reactive({
  waybillNo: '',
  status: undefined as number | undefined
})

const getStatusType = (status: number) => {
  const types: Record<number, string> = {
    1: 'info',
    2: 'warning',
    3: 'warning',
    4: 'primary',
    5: 'success',
    6: 'danger',
    7: 'info'
  }
  return types[status] || 'info'
}

const getStatusText = (status: number) => {
  const texts: Record<number, string> = {
    1: '待调度',
    2: '已调度',
    3: '提货中',
    4: '运输中',
    5: '已签收',
    6: '异常',
    7: '取消'
  }
  return texts[status] || '未知'
}

const fetchData = async () => {
  loading.value = true
  // TODO: 调用API获取数据
  setTimeout(() => {
    tableData.value = [
      {
        id: 1,
        waybillNo: 'WB202401010001',
        customerId: 1,
        customerName: '测试客户',
        totalWeight: 1000,
        totalQuantity: 10,
        shipperName: '张三',
        shipperPhone: '13800138001',
        shipperAddress: '北京市朝阳区',
        consigneeName: '李四',
        consigneePhone: '13800138002',
        consigneeAddress: '上海市浦东新区',
        waybillStatus: 1,
        createTime: '2024-01-01 10:00:00'
      }
    ]
    loading.value = false
  }, 500)
}

const handleSearch = () => {
  fetchData()
}

const handleReset = () => {
  searchForm.waybillNo = ''
  searchForm.status = undefined
  fetchData()
}

const handleAdd = () => {
  // TODO: 创建运单
}

const handleView = (row: Waybill) => {
  // TODO: 查看运单详情
}

const handleDispatch = (row: Waybill) => {
  // TODO: 调度运单
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.waybill-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}
</style>
