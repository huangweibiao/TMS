import request from '@/utils/request'
import type { Receipt, PageResult, Result } from '@/types'

export const receiptApi = {
  // 获取回单列表
  getList(params: {
    receiptNo?: string
    status?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<Result<PageResultResult<Receipt>>> {
    return request.get('/api/receipt/list', { params })
  },

  // 获取回单详情
  getById(id: number): Promise<Result<Result<Receipt>> {
    return request.get(`/api/receipt/${id}`)
  },

  // 根据回单号查询
  getByNo(receiptNo: string): Promise<Result<Result<Receipt>> {
    return request.get(`/api/receipt/by-no/${receiptNo}`)
  },

  // 根据运单ID查询回单
  getByWaybillId(waybillId: number): Promise<Result<Result<Receipt>> {
    return request.get(`/api/receipt/by-waybill/${waybillId}`)
  },

  // 创建回单
  create(data: Partial Partial<Receipt>): Promise<Result<Result<Receipt>> {
    return request.post('/api/receipt', data)
  },

  // 更新回单
  update(id: number, data: Partial Partial<Receipt>): Promise<Result<Result<Receipt>> {
    return request.put(`/api/receipt/${id}`, data)
  },

  // 删除回单
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/api/receipt/${id}`)
  },

  // 回传回单
  returnReceipt(id: number): Promise<Result<Result<Receipt>> {
    return request.post(`/api/receipt/${id}/return`)
  },

  // 审核回单
  audit(id: number): Promise<Result<Result<Receipt>> {
    return request.post(`/api/receipt/${id}/audit`)
  },

  // 生成回单号
  generateNo(): Promise<Result<{ receiptNo: string }>> {
    return request.get('/api/receipt/generate-no')
  }
}
