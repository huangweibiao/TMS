import request from '@/utils/request'
import type { Customer, PageResult, Result } from '@/types'

export const customerApi = {
  // 获取客户列表
  getList(params: {
    customerName?: string
    status?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<PageResult<Customer>>> {
    return request.get('/v1/customers', { params })
  },

  // 获取客户详情
  getById(id: number): Promise<Result<Customer>> {
    return request.get(`/v1/customers/${id}`)
  },

  // 创建客户
  create(data: Partial<Customer>): Promise<Result<Customer>> {
    return request.post('/v1/customers', data)
  },

  // 更新客户
  update(id: number, data: Partial<Customer>): Promise<Result<Customer>> {
    return request.put(`/v1/customers/${id}`, data)
  },

  // 删除客户
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/v1/customers/${id}`)
  }
}
