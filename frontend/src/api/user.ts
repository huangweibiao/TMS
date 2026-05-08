import request from '@/utils/request'
import type { User, PageResult, Result } from '@/types'

export const userApi = {
  // 获取用户列表
  getList(params: {
    username?: string
    status?: number
    pageNum?: number
    pageSize?: number
  }): Promise<Result<PageResult<User>>> {
    return request.get('/api/user/list', { params })
  },

  // 获取所有用户
  getAll(): Promise<Result<User[]>> {
    return request.get('/api/user/all')
  },

  // 获取用户详情
  getById(id: number): Promise<Result<User>> {
    return request.get(`/api/user/${id}`)
  },

  // 根据用户名查询用户
  getByUsername(username: string): Promise<Result<User>> {
    return request.get(`/api/user/by-username/${username}`)
  },

  // 创建用户
  create(data: Partial<User>): Promise<Result<User>> {
    return request.post('/api/user', data)
  },

  // 更新用户
  update(id: number, data: Partial<User>): Promise<Result<User>> {
    return request.put(`/api/user/${id}`, data)
  },

  // 删除用户
  delete(id: number): Promise<Result<void>> {
    return request.delete(`/api/user/${id}`)
  },

  // 启用用户
  enable(id: number): Promise<Result<User>> {
    return request.post(`/api/user/${id}/enable`)
  },

  // 禁用用户
  disable(id: number): Promise<Result<User>> {
    return request.post(`/api/user/${id}/disable`)
  },

  // 重置密码
  resetPassword(id: number, password: string): Promise<Result<User>> {
    return request.post(`/api/user/${id}/reset-password`, { password })
  },

  // 修改密码
  changePassword(oldPassword: string, newPassword: string): Promise<Result<{ success: boolean }>> {
    return request.post('/api/user/change-password', { oldPassword, newPassword })
  },

  // 更新用户角色
  updateRoles(id: number, roleIds: number[]): Promise<Result<void>> {
    return request.post(`/api/user/${id}/roles`, roleIds)
  },

  // 获取用户角色
  getRoles(id: number): Promise<Result<number[]>> {
    return request.get(`/api/user/${id}/roles`)
  }
}
