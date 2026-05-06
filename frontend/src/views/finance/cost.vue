<template>
  <div class="cost-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>费用管理</span>
          <el-button type="primary" @click="handleCalculate">费用计算</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="waybillNo" label="运单号" width="150" />
        <el-table-column prop="costType" label="费用类型" width="120">
          <template #default="{ row }">
            {{ getCostTypeText(row.costType) }}
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120" />
        <el-table-column prop="direction" label="方向" width="100">
          <template #default="{ row }">
            <el-tag :type="row.direction === 1 ? 'success' : 'danger'">
              {{ row.direction === 1 ? '应收' : '应付' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="settlementStatus" label="结算状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.settlementStatus === 1 ? 'warning' : 'success'">
              {{ row.settlementStatus === 1 ? '未结算' : '已结算' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

const loading = ref(false)
const tableData = ref<any[]>([])

const getCostTypeText = (type: number) => {
  const types: Record<number, string> = {
    1: '运费',
    2: '装卸费',
    3: '保险费',
    4: '等待费',
    5: '高速费',
    6: '油费',
    7: '罚款'
  }
  return types[type] || '其他'
}

const fetchData = async () => {
  loading.value = true
  // TODO: 调用API获取数据
  setTimeout(() => {
    tableData.value = [
      {
        id: 1,
        waybillNo: 'WB202401010001',
        costType: 1,
        amount: 1500.00,
        direction: 1,
        settlementStatus: 1,
        createTime: '2024-01-01 10:00:00'
      },
      {
        id: 2,
        waybillNo: 'WB202401010001',
        costType: 5,
        amount: 300.00,
        direction: 2,
        settlementStatus: 1,
        createTime: '2024-01-01 10:00:00'
      }
    ]
    loading.value = false
  }, 500)
}

const handleCalculate = () => {
  // TODO: 费用计算
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.cost-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
