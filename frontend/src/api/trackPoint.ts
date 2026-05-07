import request from '@/utils/request'
import type { TrackPoint, PageResult, Result } from '@/types'

export const trackPointApi = {
  // 创建轨迹点
  create(data: Partial Partial<TrackPoint>): Promise<Result<Result<TrackPoint>> {
    return request.post('/api/track-point', data)
  },

  // 批量创建轨迹点
  batchCreate(data: Partial Partial<TrackPoint>[]): Promise<Result<Result<TrackPoint[]>> {
    return request.post('/api/track-point/batch', data)
  },

  // 获取轨迹点详情
  getById(id: number): Promise<Result<Result<TrackPoint>> {
    return request.get(`/api/track-point/${id}`)
  },

  // 根据调度单ID查询轨迹点列表
  getByDispatchId(dispatchId: number): Promise<Result<Result<TrackPoint[]>> {
    return request.get(`/api/track-point/by-dispatch/${dispatchId}`)
  },

  // 根据调度单ID和时间范围查询轨迹点
  getByDispatchIdAndTimeRange(
    dispatchId: number,
    startTime: string,
    endTime: string
  ): Promise<Result<Result<TrackPoint[]>> {
    return request.get(`/api/track-point/by-dispatch/${dispatchId}/time-range`, {
      params: { startTime, endTime }
    })
  },

  // 根据车辆ID查询最新轨迹点
  getLatestByVehicleId(vehicleId: number, limit?: number): Promise<Result<Result<TrackPoint[]>> {
    return request.get(`/api/track-point/by-vehicle/${vehicleId}/latest`, {
      params: { limit }
    })
  },

  // 分页查询轨迹点列表
  getList(params: {
    dispatchId?: number
    vehicleId?: number
    startTime?: string
    endTime?: string
    pageNum?: number
    pageSize?: number
  }): Promise<Result<Result<PageResultResult<TrackPoint>>> {
    return request.get('/api/track-point/list', { params })
  }
}
