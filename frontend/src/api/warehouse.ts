import request from '@/utils/request'
import type { Warehouse, PageResult, Result } from '@/types'

export const warehouseApi = {
  // 获取仓库列表
  getList(params: {
    warehouseName?: string
    warehouseType?: number
    status?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<PageResult<Warehouse>>> {
    return request.get('/v1/warehouses', { params })
  },

  // 获取仓库详情
  getById(id: number): Promise<Result<Warehouse>> {
    return request.get(`/v1/warehouses/${id}`)
  },

  // 创建仓库
  create(data: Partial<Warehouse>): Promise<Result<Warehouse>> {
    return request.post('/v1/warehouses', data)
  },

  // 更新仓库
  update(id: number, data: Partial<Warehouse>): Promise<Result<Warehouse>> {
    return request.put(`/v1/warehouses/${id}`, data)
  },

  // 删除仓库
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/v1/warehouses/${id}`)
  }
}
