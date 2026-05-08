<template>
  <div class="warehouse-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>仓库管理</span>
          <el-button type="primary" @click="handleAdd">新增仓库</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="warehouseCode" label="仓库编码" width="120" />
        <el-table-column prop="warehouseName" label="仓库名称" />
        <el-table-column prop="warehouseType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.warehouseType === 1" type="success">发货仓</el-tag>
            <el-tag v-else-if="row.warehouseType === 2" type="warning">中转仓</el-tag>
            <el-tag v-else type="info">收货仓</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="city" label="城市" width="100" />
        <el-table-column prop="contactPerson" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="130" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">启用</el-tag>
            <el-tag v-else type="danger">停用</el-tag>
          </template>
        </el-table-column>
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
import type { Warehouse } from '@/types'

const loading = ref(false)
const tableData = ref<Warehouse[]>([])

const fetchData = async () => {
  loading.value = true
  // TODO: 调用API获取数据
  setTimeout(() => {
    tableData.value = [
      {
        id: 1,
        warehouseCode: 'WH001',
        warehouseName: '北京发货仓',
        warehouseType: 1,
        province: '北京市',
        city: '北京市',
        district: '朝阳区',
        address: '朝阳路1号',
        contactPerson: '王五',
        contactPhone: '13800138003',
        status: 1,
        createTime: '2024-01-01 10:00:00'
      },
      {
        id: 2,
        warehouseCode: 'WH002',
        warehouseName: '上海收货仓',
        warehouseType: 3,
        province: '上海市',
        city: '上海市',
        district: '浦东新区',
        address: '浦东大道2号',
        contactPerson: '赵六',
        contactPhone: '13800138004',
        status: 1,
        createTime: '2024-01-02 10:00:00'
      }
    ]
    loading.value = false
  }, 500)
}

const handleAdd = () => {
  // TODO: 新增仓库
}

const handleEdit = (row: Warehouse) => {
  // TODO: 编辑仓库
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.warehouse-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
