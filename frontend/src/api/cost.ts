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
  calculateType: number
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
  calculate(data: CostCalculateRequest): Promise<Result<CostDetail[]>> {
    return request.post('/v1/costs/calculate', data)
  },

  getByWaybillId(waybillId: number): Promise<Result<CostDetail[]>> {
    return request.get(`/v1/costs/waybill/${waybillId}`)
  },

  getList(params: {
    waybillNo?: string
    costType?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<PageResult<CostDetail>>> {
    return request.get('/v1/costs', { params })
  },

  create(data: Partial<CostDetail>): Promise<Result<CostDetail>> {
    return request.post('/v1/costs', data)
  },

  update(id: number, data: Partial<CostDetail>): Promise<Result<CostDetail>> {
    return request.put(`/v1/costs/${id}`, data)
  },

  delete(id: number): Promise<Result<void>> {
    return request.delete(`/v1/costs/${id}`)
  },

  getById(id: number): Promise<Result<CostDetail>> {
    return request.get(`/v1/costs/${id}`)
  }
}
