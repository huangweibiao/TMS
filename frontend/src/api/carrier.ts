import request from '@/utils/request'
import type { Carrier, PageResult, Result } from '@/types'

export const carrierApi = {
  // 获取承运商列表
  getList(params: {
    carrierName?: string
    status?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<PageResult<Carrier>>> {
    return request.get('/v1/carriers', { params })
  },

  // 获取承运商详情
  getById(id: number): Promise<Result<Carrier>> {
    return request.get(`/v1/carriers/${id}`)
  },

  // 创建承运商
  create(data: Partial<Carrier>): Promise<Result<Carrier>> {
    return request.post('/v1/carriers', data)
  },

  // 更新承运商
  update(id: number, data: Partial<Carrier>): Promise<Result<Carrier>> {
    return request.put(`/v1/carriers/${id}`, data)
  },

  // 删除承运商
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/v1/carriers/${id}`)
  }
}
