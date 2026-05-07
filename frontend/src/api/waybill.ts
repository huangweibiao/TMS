import request from '@/utils/request'
import type { Waybill, PageResult, Result } from '@/types'

export const waybillApi = {
  // 创建运单
  create(data: Partial Partial<Waybill>): Promise<Result<Result<Waybill>> {
    return request.post('/v1/waybills', data)
  },

  // 更新运单
  update(id: number, data: Partial Partial<Waybill>): Promise<Result<Result<Waybill>> {
    return request.put(`/v1/waybills/${id}`, data)
  },

  // 删除运单
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/v1/waybills/${id}`)
  },

  // 根据ID查询运单
  getById(id: number): Promise<Result<Result<Waybill>> {
    return request.get(`/v1/waybills/${id}`)
  },

  // 根据运单号查询运单
  getByNo(waybillNo: string): Promise<Result<Result<Waybill>> {
    return request.get(`/v1/waybills/no/${waybillNo}`)
  },

  // 分页查询运单列表
  getList(params: {
    waybillNo?: string
    status?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<Result<PageResultResult<Waybill>>> {
    return request.get('/v1/waybills', { params })
  },

  // 更新运单状态
  updateStatus(id: number, status: number, remark?: string): Promise<Result<void>> {
    return request.put(`/v1/waybills/${id}/status`, null, {
      params: { status, remark }
    })
  },

  // 取消运单
  cancel(id: number, remark?: string): Promise<Result<void>> {
    return request.put(`/v1/waybills/${id}/cancel`, null, {
      params: { remark }
    })
  }
}
