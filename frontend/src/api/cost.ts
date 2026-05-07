import request from '@/utils/request'
import type { PageResult, Result } from '@/types'

export interface CostDetail {
  id: number
  waybillId: number
  waybillNo?: string
  costType: number
  costTypeName?: string
  amount: number
  currency?: string
  direction: number
  directionName?: string
  settlementStatus: number
  invoiceNo?: string
  settlementId?: number
  remark?: string
  createTime?: string
  updateTime?: string
}

export interface CostCalculateRequest {
  waybillId: number
  calculateType: number // 1-按重量, 2-按体积, 3-按里程
  unitPrice: number
  distance?: number
  additionalCost?: {
    loadingFee?: number
    insuranceFee?: number
    waitingFee?: number
    tollFee?: number
    otherFee?: number
  }
  remark?: string
}

export const costApi = {
  // 计算运费
  calculate(data: CostCalculateRequest): Promise<Result<Result<CostDetail[]>> {
    return request.post('/v1/costs/calculate', data)
  },

  // 根据运单ID查询费用明细
  getByWaybillId(waybillId: number): Promise<Result<Result<CostDetail[]>> {
    return request.get(`/v1/costs/waybill/${waybillId}`)
  },

  // 分页查询费用明细
  getList(params: {
    waybillNo?: string
    costType?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<Result<PageResultResult<CostDetail>>> {
    return request.get('/v1/costs', { params })
  },

  // 创建费用明细
  create(data: Partial Partial<CostDetail>): Promise<Result<Result<CostDetail>> {
    return request.post('/v1/costs', data)
  },

  // 更新费用明细
  update(id: number, data: Partial Partial<CostDetail>): Promise<Result<Result<CostDetail>> {
    return request.put(`/v1/costs/${id}`, data)
  },

  // 删除费用明细
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/v1/costs/${id}`)
  },

  // 根据ID查询费用明细
  getById(id: number): Promise<Result<Result<CostDetail>> {
    return request.get(`/v1/costs/${id}`)
  }
}
