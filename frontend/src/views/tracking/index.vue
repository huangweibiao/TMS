<template>
  <div class="tracking-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>在途监控</span>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="16">
          <div class="map-container">
            <div class="map-placeholder">
              <el-empty description="地图区域" />
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <el-card>
            <template #header>
              <span>车辆列表</span>
            </template>
            <el-table :data="vehicleList" size="small">
              <el-table-column prop="plateNumber" label="车牌号" />
              <el-table-column prop="status" label="状态" width="80">
                <template #default="{ row }">
                  <el-tag size="small" :type="row.status === 2 ? 'primary' : 'success'">
                    {{ row.status === 2 ? '在途' : '空闲' }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

const vehicleList = ref<any[]>([])

const fetchData = async () => {
  // TODO: 调用API获取数据
  vehicleList.value = [
    { id: 1, plateNumber: '京A12345', status: 2 },
    { id: 2, plateNumber: '京B67890', status: 1 },
    { id: 3, plateNumber: '京C11111', status: 1 }
  ]
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.tracking-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.map-container {
  height: 500px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.map-placeholder {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
