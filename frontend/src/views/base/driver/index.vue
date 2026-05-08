<template>
  <div class="driver-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>司机管理</span>
          <el-button type="primary" @click="handleAdd">新增司机</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="driverName" label="姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="licenseType" label="驾照类型" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">空闲</el-tag>
            <el-tag v-else-if="row.status === 2" type="warning">在途</el-tag>
            <el-tag v-else-if="row.status === 3" type="info">休息</el-tag>
            <el-tag v-else type="danger">离职</el-tag>
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
import type { Driver } from '@/types'

const loading = ref(false)
const tableData = ref<Driver[]>([])

const fetchData = async () => {
  loading.value = true
  // TODO: 调用API获取数据
  setTimeout(() => {
    tableData.value = [
      {
        id: 1,
        driverName: '张三',
        idCard: '110101199001011234',
        phone: '13800138001',
        licenseType: 'A2',
        status: 1,
        createTime: '2024-01-01 10:00:00'
      },
      {
        id: 2,
        driverName: '李四',
        idCard: '110101199002021234',
        phone: '13800138002',
        licenseType: 'B2',
        status: 2,
        createTime: '2024-01-02 10:00:00'
      }
    ]
    loading.value = false
  }, 500)
}

const handleAdd = () => {
  // TODO: 新增司机
}

const handleEdit = (row: Driver) => {
  // TODO: 编辑司机
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.driver-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
