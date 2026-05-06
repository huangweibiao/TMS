<template>
  <div class="settlement-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>结算管理</span>
          <el-button type="primary" @click="handleCreate">创建结算单</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="settlementNo" label="结算单号" width="150" />
        <el-table-column prop="partyName" label="结算方" />
        <el-table-column prop="partyType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.partyType === 1 ? 'primary' : 'success'">
              {{ row.partyType === 1 ? '客户' : '承运商' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="120" />
        <el-table-column prop="paidAmount" label="已付金额" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="success" link @click="handleConfirm(row)" v-if="row.status === 1">确认</el-button>
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
    4: 'success',
    5: 'info'
  }
  return types[status] || 'info'
}

const getStatusText = (status: number) => {
  const texts: Record<number, string> = {
    1: '待确认',
    2: '已确认',
    3: '部分付款',
    4: '已结清',
    5: '已取消'
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
        settlementNo: 'ST202401010001',
        partyName: '测试客户',
        partyType: 1,
        totalAmount: 1500.00,
        paidAmount: 0.00,
        status: 1,
        createTime: '2024-01-01 10:00:00'
      }
    ]
    loading.value = false
  }, 500)
}

const handleCreate = () => {
  // TODO: 创建结算单
}

const handleView = (row: any) => {
  // TODO: 查看结算单详情
}

const handleConfirm = (row: any) => {
  // TODO: 确认结算单
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.settlement-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
