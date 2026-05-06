<template>
  <div class="role-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="handleAdd">新增角色</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="roleCode" label="角色编码" width="150" />
        <el-table-column prop="roleName" label="角色名称" width="150" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="primary" link @click="handlePermission(row)">权限</el-button>
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

const fetchData = async () => {
  loading.value = true
  // TODO: 调用API获取数据
  setTimeout(() => {
    tableData.value = [
      {
        id: 1,
        roleCode: 'ROLE_ADMIN',
        roleName: '系统管理员',
        description: '拥有所有权限',
        status: 1
      },
      {
        id: 2,
        roleCode: 'ROLE_DISPATCHER',
        roleName: '调度员',
        description: '负责运单调度和派车',
        status: 1
      },
      {
        id: 3,
        roleCode: 'ROLE_CUSTOMER_SERVICE',
        roleName: '客服',
        description: '负责运单创建和跟踪',
        status: 1
      },
      {
        id: 4,
        roleCode: 'ROLE_FINANCE',
        roleName: '财务',
        description: '负责费用结算和对账',
        status: 1
      }
    ]
    loading.value = false
  }, 500)
}

const handleAdd = () => {
  // TODO: 新增角色
}

const handleEdit = (row: any) => {
  // TODO: 编辑角色
}

const handlePermission = (row: any) => {
  // TODO: 配置权限
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.role-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
