<template>
  <div class="dispatch-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>调度管理</span>
          <el-button type="primary" @click="handleDispatch">智能派车</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="dispatchNo" label="调度单号" width="150" />
        <el-table-column prop="waybillNo" label="运单号" width="150" />
        <el-table-column prop="plateNumber" label="车牌号" width="120" />
        <el-table-column prop="driverName" label="司机" width="100" />
        <el-table-column prop="plannedStartTime" label="计划发车" width="160" />
        <el-table-column prop="plannedEndTime" label="计划到达" width="160" />
        <el-table-column prop="dispatchStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.dispatchStatus)">{{ getStatusText(row.dispatchStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="success" link @click="handleStart(row)" v-if="row.dispatchStatus === 1">发车</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

const loading = ref(false)
const tableData = ref<any[]>([])

const getStatusType = (status: number) => {
  const types: Record<number, string> = {
    1: 'warning',
    2: 'primary',
    3: 'success',
    4: 'info'
  }
  return types[status] || 'info'
}

const getStatusText = (status: number) => {
  const texts: Record<number, string> = {
    1: '待发车',
    2: '已发车',
    3: '已完成',
    4: '已取消'
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
        dispatchNo: 'DP202401010001',
        waybillNo: 'WB202401010001',
        plateNumber: '京A12345',
        driverName: '张三',
        plannedStartTime: '2024-01-01 08:00:00',
        plannedEndTime: '2024-01-02 18:00:00',
        dispatchStatus: 1
      }
    ]
    loading.value = false
  }, 500)
}

const handleDispatch = () => {
  // TODO: 智能派车
}

const handleView = (row: any) => {
  // TODO: 查看调度单详情
}

const handleStart = (row: any) => {
  // TODO: 确认发车
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.dispatch-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
