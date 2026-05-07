import request from '@/utils/request'
import type { Driver, PageResult, Result } from '@/types'

export const driverApi = {
  // 获取司机列表
  getList(params: {
    driverName?: string
    phone?: string
    status?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<Result<PageResultResult<Driver>>> {
    return request.get('/v1/drivers', { params })
  },

  // 获取司机详情
  getById(id: number): Promise<Result<Result<Driver>> {
    return request.get(`/v1/drivers/${id}`)
  },

  // 获取可用司机列表
  getAvailable(): Promise<Result<Result<Driver[]>> {
    return request.get('/v1/drivers/available')
  },

  // 创建司机
  create(data: Partial Partial<Driver>): Promise<Result<Result<Driver>> {
    return request.post('/v1/drivers', data)
  },

  // 更新司机
  update(id: number, data: Partial Partial<Driver>): Promise<Result<Result<Driver>> {
    return request.put(`/v1/drivers/${id}`, data)
  },

  // 删除司机
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/v1/drivers/${id}`)
  }
}
