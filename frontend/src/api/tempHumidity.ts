import request from '@/utils/request'
import type { TempHumidity, PageResult, Result } from '@/types'

export const tempHumidityApi = {
  // 创建温湿度记录
  create(data: Partial<TempHumidity>): Promise<Result<TempHumidity>> {
    return request.post('/api/temp-humidity', data)
  },

  // 批量创建温湿度记录
  batchCreate(data: Partial<TempHumidity>[]): Promise<Result<TempHumidity[]>> {
    return request.post('/api/temp-humidity/batch', data)
  },

  // 获取温湿度记录详情
  getById(id: number): Promise<Result<TempHumidity>> {
    return request.get(`/api/temp-humidity/${id}`)
  },

  // 根据调度单ID查询温湿度记录列表
  getByDispatchId(dispatchId: number): Promise<Result<TempHumidity[]>> {
    return request.get(`/api/temp-humidity/by-dispatch/${dispatchId}`)
  },

  // 根据调度单ID和时间范围查询温湿度记录
  getByDispatchIdAndTimeRange(
    dispatchId: number,
    startTime: string,
    endTime: string
  ): Promise<Result<TempHumidity[]>> {
    return request.get(`/api/temp-humidity/by-dispatch/${dispatchId}/time-range`, {
      params: { startTime, endTime }
    })
  },

  // 分页查询温湿度记录
  getList(params: {
    dispatchId?: number
    deviceId?: string
    startTime?: string
    endTime?: string
    pageNum?: number
    pageSize?: number
  }): Promise<Result<Result<PageResult<TempHumidity>>> {
    return request.get('/api/temp-humidity/list', { params })
  },

  // 获取最新温湿度记录
  getLatest(dispatchId: number): Promise<Result<TempHumidity>> {
    return request.get(`/api/temp-humidity/latest/${dispatchId}`)
  },

  // 检查温度是否异常
  checkTemperature(
    dispatchId: number,
    temperature: number,
    minTemp: number,
    maxTemp: number
  ): Promise<Result<{ isAbnormal: boolean; temperature: number; minTemp: number; maxTemp: number }>> {
    return request.get('/api/temp-humidity/check-temperature', {
      params: { dispatchId, temperature, minTemp, maxTemp }
    })
  }
}
