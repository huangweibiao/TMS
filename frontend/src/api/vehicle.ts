import request from '@/utils/request'
import type { Vehicle, PageResult, Result } from '@/types'

export const vehicleApi = {
  // 获取车辆列表
  getList(params: {
    plateNumber?: string
    vehicleType?: string
    status?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<Result<PageResult<Vehicle>>> {
    return request.get('/v1/vehicles', { params })
  },

  // 获取车辆详情
  getById(id: number): Promise<Result<Vehicle>> {
    return request.get(`/v1/vehicles/${id}`)
  },

  // 获取可用车辆列表
  getAvailable(): Promise<Result<Vehicle[]>> {
    return request.get('/v1/vehicles/available')
  },

  // 创建车辆
  create(data: Partial<Vehicle>): Promise<Result<Vehicle>> {
    return request.post('/v1/vehicles', data)
  },

  // 更新车辆
  update(id: number, data: Partial<Vehicle>): Promise<Result<Vehicle>> {
    return request.put(`/v1/vehicles/${id}`, data)
  },

  // 删除车辆
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/v1/vehicles/${id}`)
  }
}
