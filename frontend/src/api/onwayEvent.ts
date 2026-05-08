import request from '@/utils/request'
import type { OnwayEvent, PageResult, Result } from '@/types'

export const onwayEventApi = {
  // 获取在途事件列表
  getList(params: {
    dispatchId?: number
    eventType?: number
    eventLevel?: number
    isHandled?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<PageResult<OnwayEvent>>> {
    return request.get('/api/onway-event/list', { params })
  },

  // 获取在途事件详情
  getById(id: number): Promise<Result<OnwayEvent>> {
    return request.get(`/api/onway-event/${id}`)
  },

  // 根据调度单ID查询在途事件列表
  getByDispatchId(dispatchId: number): Promise<Result<OnwayEvent[]>> {
    return request.get(`/api/onway-event/by-dispatch/${dispatchId}`)
  },

  // 创建在途事件
  create(data: Partial<OnwayEvent>): Promise<Result<OnwayEvent>> {
    return request.post('/api/onway-event', data)
  },

  // 更新在途事件
  update(id: number, data: Partial<OnwayEvent>): Promise<Result<OnwayEvent>> {
    return request.put(`/api/onway-event/${id}`, data)
  },

  // 删除在途事件
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/api/onway-event/${id}`)
  },

  // 处理事件
  handleEvent(id: number, handleResult: string): Promise<Result<OnwayEvent>> {
    return request.post(`/api/onway-event/${id}/handle`, { handleResult })
  },

  // 获取未处理事件数量
  getUnhandledCount(): Promise<Result<{ count: number }>> {
    return request.get('/api/onway-event/unhandled-count')
  }
}
