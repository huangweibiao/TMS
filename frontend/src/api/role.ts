import request from '@/utils/request'
import type { Role, Result } from '@/types'

export const roleApi = {
  // 获取角色列表
  getList(): Promise<Result<Result<Role[]>> {
    return request.get('/api/role/list')
  },

  // 获取角色详情
  getById(id: number): Promise<Result<Result<Role>> {
    return request.get(`/api/role/${id}`)
  },

  // 根据角色编码查询角色
  getByCode(roleCode: string): Promise<Result<Result<Role>> {
    return request.get(`/api/role/by-code/${roleCode}`)
  },

  // 根据状态查询角色列表
  getByStatus(status: number): Promise<Result<Result<Role[]>> {
    return request.get(`/api/role/by-status/${status}`)
  },

  // 创建角色
  create(data: Partial Partial<Role>): Promise<Result<Result<Role>> {
    return request.post('/api/role', data)
  },

  // 更新角色
  update(id: number, data: Partial Partial<Role>): Promise<Result<Result<Role>> {
    return request.put(`/api/role/${id}`, data)
  },

  // 删除角色
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/api/role/${id}`)
  },

  // 启用角色
  enable(id: number): Promise<Result<Result<Role>> {
    return request.post(`/api/role/${id}/enable`)
  },

  // 禁用角色
  disable(id: number): Promise<Result<Result<Role>> {
    return request.post(`/api/role/${id}/disable`)
  },

  // 更新角色权限
  updatePermissions(id: number, permissionIds: number[]): Promise<Result<void>> {
    return request.post(`/api/role/${id}/permissions`, permissionIds)
  },

  // 获取角色权限
  getPermissions(id: number): Promise<Result<number[]>> {
    return request.get(`/api/role/${id}/permissions`)
  },

  // 获取用户角色
  getByUserId(userId: number): Promise<Result<Result<Role[]>> {
    return request.get(`/api/role/by-user/${userId}`)
  }
}
