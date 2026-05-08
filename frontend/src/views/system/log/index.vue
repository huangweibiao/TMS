<template>
  <div class="log-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>操作日志</span>
          <el-button type="danger" @click="handleClear">清空日志</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select v-model="searchForm.operationType" placeholder="请选择操作类型" clearable>
            <el-option label="新增" value="新增" />
            <el-option label="修改" value="修改" />
            <el-option label="删除" value="删除" />
            <el-option label="查询" value="查询" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="operationType" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getOperationTypeTag(row.operationType)">
              {{ row.operationType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationDesc" label="操作描述" />
        <el-table-column prop="requestUrl" label="请求URL" show-overflow-tooltip />
        <el-table-column prop="ipAddress" label="IP地址" width="130" />
        <el-table-column prop="costTime" label="耗时(ms)" width="100" />
        <el-table-column prop="isSuccess" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.isSuccess === 1 ? 'success' : 'danger'">
              {{ row.isSuccess === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="180" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="dialogVisible" title="日志详情" width="700px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="用户名">{{ currentLog?.username }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">{{ currentLog?.operationType }}</el-descriptions-item>
        <el-descriptions-item label="操作描述">{{ currentLog?.operationDesc }}</el-descriptions-item>
        <el-descriptions-item label="请求URL">{{ currentLog?.requestUrl }}</el-descriptions-item>
        <el-descriptions-item label="请求参数">
          <pre>{{ formatJson(currentLog?.requestParams) }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="响应结果">{{ currentLog?.responseResult }}</el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ currentLog?.ipAddress }}</el-descriptions-item>
        <el-descriptions-item label="耗时">{{ currentLog?.costTime }}ms</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentLog?.isSuccess === 1 ? 'success' : 'danger'">
            {{ currentLog?.isSuccess === 1 ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="错误信息" v-if="currentLog?.errorMsg">
          {{ currentLog?.errorMsg }}
        </el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ currentLog?.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { logApi, type OperationLog } from '@/api/log'

const loading = ref(false)
const tableData = ref<OperationLog[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const currentLog = ref<OperationLog | null>(null)

const searchForm = reactive({
  username: '',
  operationType: '',
  dateRange: [] as string[]
})

const getOperationTypeTag = (type: string) => {
  const tags: Record<string, string> = {
    '新增': 'success',
    '修改': 'warning',
    '删除': 'danger',
    '查询': 'info'
  }
  return tags[type] || 'info'
}

const formatJson = (json?: string) => {
  if (!json) return ''
  try {
    return JSON.stringify(JSON.parse(json), null, 2)
  } catch {
    return json
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const params: any = {
      username: searchForm.username,
      operationType: searchForm.operationType,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startTime = searchForm.dateRange[0]
      params.endTime = searchForm.dateRange[1]
    }
    const res = await logApi.getList(params)
    tableData.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pageNum.value = 1
  fetchData()
}

const handleReset = () => {
  searchForm.username = ''
  searchForm.operationType = ''
  searchForm.dateRange = []
  handleSearch()
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  fetchData()
}

const handleCurrentChange = (val: number) => {
  pageNum.value = val
  fetchData()
}

const handleView = (row: OperationLog) => {
  currentLog.value = row
  dialogVisible.value = true
}

const handleDelete = async (row: OperationLog) => {
  try {
    await ElMessageBox.confirm('确定要删除该日志吗？', '提示', {
      type: 'warning'
    })
    await logApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    // 用户取消
  }
}

const handleClear = async () => {
  try {
    await ElMessageBox.confirm('确定要清空所有日志吗？此操作不可恢复！', '警告', {
      type: 'danger',
      confirmButtonClass: 'el-button--danger'
    })
    await logApi.clear()
    ElMessage.success('清空成功')
    fetchData()
  } catch (error) {
    // 用户取消
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.log-page {
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

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

pre {
  margin: 0;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  max-height: 200px;
  overflow: auto;
}
</style>
