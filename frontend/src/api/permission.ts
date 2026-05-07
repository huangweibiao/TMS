import request from '@/utils/request'
import type { Permission, Result } from '@/types'

export const permissionApi = {
  // 获取权限列表
  getList(): Promise<Result<Result<Permission[]>> {
    return request.get('/api/permission/list')
  },

  // 获取权限详情
  getById(id: number): Promise<Result<Result<Permission>> {
    return request.get(`/api/permission/${id}`)
  },

  // 根据权限编码查询
  getByCode(permissionCode: string): Promise<Result<Result<Permission>> {
    return request.get(`/api/permission/by-code/${permissionCode}`)
  },

  // 根据父权限ID查询权限列表
  getByParentId(parentId: number): Promise<Result<Result<Permission[]>> {
    return request.get(`/api/permission/by-parent/${parentId}`)
  },

  // 根据权限类型查询权限列表
  getByType(permissionType: number): Promise<Result<Result<Permission[]>> {
    return request.get(`/api/permission/by-type/${permissionType}`)
  },

  // 创建权限
  create(data: Partial Partial<Permission>): Promise<Result<Result<Permission>> {
    return request.post('/api/permission', data)
  },

  // 更新权限
  update(id: number, data: Partial Partial<Permission>): Promise<Result<Result<Permission>> {
    return request.put(`/api/permission/${id}`, data)
  },

  // 删除权限
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/api/permission/${id}`)
  },

  // 启用权限
  enable(id: number): Promise<Result<Result<Permission>> {
    return request.post(`/api/permission/${id}/enable`)
  },

  // 禁用权限
  disable(id: number): Promise<Result<Result<Permission>> {
    return request.post(`/api/permission/${id}/disable`)
  }
}
