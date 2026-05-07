import request from '@/utils/request'
import type { Result } from '@/types'

export interface ReportData {
  date: string
  waybillCount: number
  totalDistance: number
  freightAmount: number
  costAmount: number
  profit: number
  exceptionCount: number
}

export interface KpiData {
  todayWaybillCount: number
  monthWaybillCount: number
  inTransitVehicles: number
  pendingExceptions: number
  monthIncome: number
}

export const reportApi = {
  // 运单统计报表
  getWaybillStatistics(params: {
    startDate: string
    endDate: string
  }): Promise<Result<Result<ReportData[]>> {
    return request.get('/v1/reports/waybill-stat', { params })
  },

  // 成本分析报表
  getCostAnalysis(params: {
    startDate: string
    endDate: string
  }): Promise<Result<Result<{
    costByType: Record<number, number>
    totalIncome: number
    totalExpense: number
    profit: number
  }>> {
    return request.get('/v1/reports/cost-analysis', { params })
  },

  // 运输时效统计
  getTransportEfficiency(params: {
    startDate: string
    endDate: string
  }): Promise<Result<Result<{
    totalDispatches: number
    totalDistance: number
    avgTransportTime: number
    completedCount: number
  }>> {
    return request.get('/v1/reports/transport-efficiency', { params })
  },

  // 车辆利用率统计
  getVehicleUtilization(params: {
    startDate: string
    endDate: string
  }): Promise<Result<Result<{
    totalVehicles: number
    availableVehicles: number
    inTransitVehicles: number
    maintenanceVehicles: number
    utilizationRate: number
  }>> {
    return request.get('/v1/reports/vehicle-utilization', { params })
  },

  // 异常统计
  getExceptionStatistics(params: {
    startDate: string
    endDate: string
  }): Promise<Result<Result<{
    totalExceptions: number
    exceptionByType: Record<number, number>
    handledCount: number
    unhandledCount: number
  }>> {
    return request.get('/v1/reports/exception-statistics', { params })
  },

  // KPI看板数据
  getKpiDashboard(): Promise<Result<KpiData>> {
    return request.get('/v1/reports/kpi-dashboard')
  }
}
