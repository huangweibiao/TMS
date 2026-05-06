<template>
  <div class="vehicle-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>车辆管理</span>
          <el-button type="primary" @click="handleAdd">新增车辆</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="plateNumber" label="车牌号" width="120" />
        <el-table-column prop="vehicleType" label="车型" width="100" />
        <el-table-column prop="vehicleBrand" label="品牌" width="100" />
        <el-table-column prop="loadCapacity" label="载重(吨)" width="100" />
        <el-table-column prop="volumeCapacity" label="容积(m³)" width="100" />
        <el-table-column prop="ownerType" label="所属" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.ownerType === 1" type="success">自有</el-tag>
            <el-tag v-else type="warning">外协</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">可用</el-tag>
            <el-tag v-else-if="row.status === 2" type="warning">维修</el-tag>
            <el-tag v-else type="danger">报废</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Vehicle } from '@/types'

const loading = ref(false)
const tableData = ref<Vehicle[]>([])

const fetchData = async () => {
  loading.value = true
  // TODO: 调用API获取数据
  setTimeout(() => {
    tableData.value = [
      {
        id: 1,
        plateNumber: '京A12345',
        vehicleType: '厢式货车',
        vehicleBrand: '东风',
        loadCapacity: 5,
        volumeCapacity: 20,
        ownerType: 1,
        status: 1,
        createTime: '2024-01-01 10:00:00'
      },
      {
        id: 2,
        plateNumber: '京B67890',
        vehicleType: '平板车',
        vehicleBrand: '解放',
        loadCapacity: 10,
        volumeCapacity: 0,
        ownerType: 1,
        status: 1,
        createTime: '2024-01-02 10:00:00'
      }
    ]
    loading.value = false
  }, 500)
}

const handleAdd = () => {
  // TODO: 新增车辆
}

const handleEdit = (row: Vehicle) => {
  // TODO: 编辑车辆
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.vehicle-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
