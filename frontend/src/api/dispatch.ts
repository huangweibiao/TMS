import request from '@/utils/request'
import type { PageResult, Result } from '@/types'

export interface Dispatch {
  id: number
  dispatchNo: string
  waybillId: number
  waybillNo?: string
  vehicleId: number
  plateNumber?: string
  driverId: number
  driverName?: string
  carrierId?: number
  routePlan?: string
  plannedDistance?: number
  actualDistance?: number
  plannedStartTime?: string
  plannedEndTime?: string
  actualStartTime?: string
  actualEndTime?: string
  dispatchStatus: number
  createBy?: string
  createTime?: string
  updateTime?: string
}

export const dispatchApi = {
  // 智能派车
  assign(params: {
    waybillId: number
    vehicleId: number
    driverId: number
    strategy?: number
  }): Promise<Result<Dispatch>> {
    return request.post('/v1/dispatches/assign', null, { params })
  },

  // 手动创建调度单
  create(data: Partial<Dispatch>): Promise<Result<Dispatch>> {
    return request.post('/v1/dispatches', data)
  },

  // 根据ID查询调度单
  getById(id: number): Promise<Result<Dispatch>> {
    return request.get(`/v1/dispatches/${id}`)
  },

  // 根据运单ID查询调度单
  getByWaybillId(waybillId: number): Promise<Result<Dispatch>> {
    return request.get(`/v1/dispatches/waybill/${waybillId}`)
  },

  // 分页查询调度单列表
  getList(params: {
    dispatchNo?: string
    status?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<PageResult<Dispatch>>> {
    return request.get('/v1/dispatches', { params })
  },

  // 确认发车
  start(id: number): Promise<Result<void>> {
    return request.put(`/v1/dispatches/${id}/start`)
  },

  // 完成调度
  complete(id: number): Promise<Result<void>> {
    return request.put(`/v1/dispatches/${id}/complete`)
  },

  // 取消调度
  cancel(id: number, reason?: string): Promise<Result<void>> {
    return request.put(`/v1/dispatches/${id}/cancel`, null, {
      params: { reason }
    })
  }
}
