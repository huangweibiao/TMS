import request from '@/utils/request'
import type { LoadingOrder, PageResult, Result } from '@/types'

export const loadingOrderApi = {
  // 获取装货单列表
  getList(params: {
    loadingNo?: string
    status?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<PageResult<LoadingOrder>>> {
    return request.get('/api/loading-order/list', { params })
  },

  // 获取装货单详情
  getById(id: number): Promise<Result<LoadingOrder>> {
    return request.get(`/api/loading-order/${id}`)
  },

  // 根据装货单号查询
  getByNo(loadingNo: string): Promise<Result<LoadingOrder>> {
    return request.get(`/api/loading-order/by-no/${loadingNo}`)
  },

  // 根据调度单ID查询装货单列表
  getByDispatchId(dispatchId: number): Promise<Result<LoadingOrder[]>> {
    return request.get(`/api/loading-order/by-dispatch/${dispatchId}`)
  },

  // 创建装货单
  create(data: Partial<LoadingOrder>): Promise<Result<LoadingOrder>> {
    return request.post('/api/loading-order', data)
  },

  // 更新装货单
  update(id: number, data: Partial<LoadingOrder>): Promise<Result<LoadingOrder>> {
    return request.put(`/api/loading-order/${id}`, data)
  },

  // 删除装货单
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/api/loading-order/${id}`)
  },

  // 开始装货
  startLoading(id: number): Promise<Result<LoadingOrder>> {
    return request.post(`/api/loading-order/${id}/start`)
  },

  // 完成装货
  completeLoading(id: number): Promise<Result<LoadingOrder>> {
    return request.post(`/api/loading-order/${id}/complete`)
  },

  // 生成装货单号
  generateNo(): Promise<Result<{ loadingNo: string }>> {
    return request.get('/api/loading-order/generate-no')
  }
}
