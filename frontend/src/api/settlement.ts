import request from '@/utils/request'
import type { Settlement, PageResult, Result } from '@/types'

export const settlementApi = {
  // 获取结算单列表
  getList(params: {
    partyType?: number
    partyId?: number
    status?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<PageResult<Settlement>>> {
    return request.get('/api/settlement/list', { params })
  },

  // 获取结算单详情
  getById(id: number): Promise<Result<Settlement>> {
    return request.get(`/api/settlement/${id}`)
  },

  // 根据结算单号查询
  getByNo(settlementNo: string): Promise<Result<Settlement>> {
    return request.get(`/api/settlement/by-no/${settlementNo}`)
  },

  // 创建结算单
  create(data: Partial<Settlement>): Promise<Result<Settlement>> {
    return request.post('/api/settlement', data)
  },

  // 更新结算单
  update(id: number, data: Partial<Settlement>): Promise<Result<Settlement>> {
    return request.put(`/api/settlement/${id}`, data)
  },

  // 删除结算单
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/api/settlement/${id}`)
  },

  // 确认结算单
  confirm(id: number): Promise<Result<Settlement>> {
    return request.post(`/api/settlement/${id}/confirm`)
  },

  // 付款
  makePayment(id: number, amount: number): Promise<Result<Settlement>> {
    return request.post(`/api/settlement/${id}/payment`, { amount })
  },

  // 取消结算单
  cancel(id: number, reason?: string): Promise<Result<Settlement>> {
    return request.post(`/api/settlement/${id}/cancel`, { reason })
  },

  // 生成结算单号
  generateNo(): Promise<Result<{ settlementNo: string }>> {
    return request.get('/api/settlement/generate-no')
  }
}
