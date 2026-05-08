<template>
  <div class="report-page">
    <!-- KPI 看板 -->
    <el-row :gutter="20" class="kpi-cards">
      <el-col :span="4">
        <el-card>
          <div class="kpi-item">
            <div class="kpi-value">{{ kpiData.todayWaybillCount }}</div>
            <div class="kpi-label">今日运单</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card>
          <div class="kpi-item">
            <div class="kpi-value">{{ kpiData.monthWaybillCount }}</div>
            <div class="kpi-label">本月运单</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card>
          <div class="kpi-item">
            <div class="kpi-value">{{ kpiData.inTransitVehicles }}</div>
            <div class="kpi-label">在途车辆</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card>
          <div class="kpi-item">
            <div class="kpi-value">{{ kpiData.pendingExceptions }}</div>
            <div class="kpi-label">待处理异常</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card>
          <div class="kpi-item">
            <div class="kpi-value">¥{{ formatMoney(kpiData.monthIncome) }}</div>
            <div class="kpi-label">本月收入</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card>
          <div class="kpi-item">
            <div class="kpi-value">{{ vehicleUtilization.utilizationRate?.toFixed(1) }}%</div>
            <div class="kpi-label">车辆利用率</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 日期筛选 -->
    <el-card class="filter-card">
      <el-form :inline="true">
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="handleDateChange"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 运单统计 -->
    <el-card class="chart-card">
      <template #header>
        <span>运单统计</span>
      </template>
      <div class="chart-placeholder">
        <el-table :data="waybillStat" border size="small">
          <el-table-column prop="date" label="日期" />
          <el-table-column prop="waybillCount" label="运单数量" />
          <el-table-column prop="freightAmount" label="运费金额">
            <template #default="{ row }">
              ¥{{ formatMoney(row.freightAmount) }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 成本分析 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>成本分析</span>
          </template>
          <div class="cost-summary">
            <div class="cost-item income">
              <div class="cost-label">总收入</div>
              <div class="cost-value">¥{{ formatMoney(costAnalysis.totalIncome) }}</div>
            </div>
            <div class="cost-item expense">
              <div class="cost-label">总支出</div>
              <div class="cost-value">¥{{ formatMoney(costAnalysis.totalExpense) }}</div>
            </div>
            <div class="cost-item profit">
              <div class="cost-label">利润</div>
              <div class="cost-value">¥{{ formatMoney(costAnalysis.profit) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>车辆利用率</span>
          </template>
          <div class="vehicle-stats">
            <div class="stat-item">
              <div class="stat-label">总车辆</div>
              <div class="stat-value">{{ vehicleUtilization.totalVehicles }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">可用车辆</div>
              <div class="stat-value">{{ vehicleUtilization.availableVehicles }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">在途车辆</div>
              <div class="stat-value">{{ vehicleUtilization.inTransitVehicles }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">维修车辆</div>
              <div class="stat-value">{{ vehicleUtilization.maintenanceVehicles }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 异常统计 -->
    <el-card class="chart-card">
      <template #header>
        <span>异常统计</span>
      </template>
      <div class="exception-stats">
        <div class="exception-item">
          <div class="exception-label">异常总数</div>
          <div class="exception-value">{{ exceptionStat.totalExceptions }}</div>
        </div>
        <div class="exception-item">
          <div class="exception-label">已处理</div>
          <div class="exception-value success">{{ exceptionStat.handledCount }}</div>
        </div>
        <div class="exception-item">
          <div class="exception-label">待处理</div>
          <div class="exception-value danger">{{ exceptionStat.unhandledCount }}</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { reportApi, type ReportData, type KpiData } from '@/api/report'
import { formatDate } from '@/utils/format'

const dateRange = ref<string[]>([])
const kpiData = reactive<KpiData>({
  todayWaybillCount: 0,
  monthWaybillCount: 0,
  inTransitVehicles: 0,
  pendingExceptions: 0,
  monthIncome: 0
})

const waybillStat = ref<ReportData[]>([])
const costAnalysis = reactive({
  totalIncome: 0,
  totalExpense: 0,
  profit: 0
})
const vehicleUtilization = reactive({
  totalVehicles: 0,
  availableVehicles: 0,
  inTransitVehicles: 0,
  maintenanceVehicles: 0,
  utilizationRate: 0
})
const exceptionStat = reactive({
  totalExceptions: 0,
  handledCount: 0,
  unhandledCount: 0
})

const formatMoney = (amount?: number) => {
  if (amount === undefined || amount === null) return '0.00'
  return amount.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const handleDateChange = () => {
  fetchData()
}

const fetchData = async () => {
  // 设置默认时间范围（最近7天）
  let startDate = dateRange.value?.[0]
  let endDate = dateRange.value?.[1]
  
  if (!startDate || !endDate) {
    const end = new Date()
    const start = new Date()
    start.setDate(start.getDate() - 7)
    startDate = formatDate(start, 'YYYY-MM-DD')
    endDate = formatDate(end, 'YYYY-MM-DD')
    dateRange.value = [startDate, endDate]
  }

  const params = { startDate, endDate }

  try {
    // 获取KPI数据
    const kpiRes = await reportApi.getKpiDashboard()
    Object.assign(kpiData, kpiRes.data)

    // 获取运单统计
    const waybillRes = await reportApi.getWaybillStatistics(params)
    waybillStat.value = waybillRes.data

    // 获取成本分析
    const costRes = await reportApi.getCostAnalysis(params)
    Object.assign(costAnalysis, costRes.data)

    // 获取车辆利用率
    const vehicleRes = await reportApi.getVehicleUtilization(params)
    Object.assign(vehicleUtilization, vehicleRes.data)

    // 获取异常统计
    const exceptionRes = await reportApi.getExceptionStatistics(params)
    Object.assign(exceptionStat, exceptionRes.data)
  } catch (error) {
    console.error('获取报表数据失败', error)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.report-page {
  padding: 20px;
}

.kpi-cards {
  margin-bottom: 20px;
}

.kpi-item {
  text-align: center;
  padding: 10px 0;
}

.kpi-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.kpi-label {
  font-size: 14px;
  color: #606266;
}

.filter-card {
  margin-bottom: 20px;
}

.chart-card {
  margin-bottom: 20px;
}

.chart-row {
  margin-bottom: 20px;
}

.cost-summary {
  display: flex;
  justify-content: space-around;
  padding: 20px 0;
}

.cost-item {
  text-align: center;
}

.cost-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
}

.cost-value {
  font-size: 20px;
  font-weight: bold;
}

.cost-item.income .cost-value {
  color: #67c23a;
}

.cost-item.expense .cost-value {
  color: #f56c6c;
}

.cost-item.profit .cost-value {
  color: #409eff;
}

.vehicle-stats {
  display: flex;
  justify-content: space-around;
  padding: 20px 0;
}

.stat-item {
  text-align: center;
}

.stat-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}

.exception-stats {
  display: flex;
  justify-content: space-around;
  padding: 20px 0;
}

.exception-item {
  text-align: center;
}

.exception-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
}

.exception-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}

.exception-value.success {
  color: #67c23a;
}

.exception-value.danger {
  color: #f56c6c;
}
</style>
