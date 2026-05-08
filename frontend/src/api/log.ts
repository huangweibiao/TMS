import request from '@/utils/request'
import type { PageResult, Result } from '@/types'

export interface OperationLog {
  id: number
  userId?: number
  username?: string
  operationType: string
  operationDesc: string
  requestUrl: string
  requestParams?: string
  responseResult?: string
  ipAddress?: string
  costTime?: number
  isSuccess: number
  errorMsg?: string
  createTime: string
}

export const logApi = {
  // 分页查询操作日志
  getList(params: {
    username?: string
    operationType?: string
    startTime?: string
    endTime?: string
    pageNum?: number
    pageSize?: number
  }): Promise<Result<PageResult<OperationLog>>> {
    return request.get('/v1/logs', { params })
  },

  // 根据ID查询操作日志
  getById(id: number): Promise<Result<OperationLog>> {
    return request.get(`/v1/logs/${id}`)
  },

  // 删除操作日志
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/v1/logs/${id}`)
  },

  // 批量删除操作日志
  deleteBatch(ids: number[]): Promise<Result<void>> {
    return request.delete('/v1/logs/batch', { data: ids })
  },

  // 清空操作日志
  clear(): Promise<Result<void>> {
    return request.delete('/v1/logs/clear')
  }
}
