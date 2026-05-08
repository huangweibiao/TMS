import request from '@/utils/request'
import type { LoginRequest, LoginResponse, Result } from '@/types'

export const authApi = {
  // 登录
  login(data: LoginRequest): Promise<Result<LoginResponse>> {
    return request.post('/v1/auth/login', data)
  },

  // 登出
  logout(): Promise<Result<void>> {
    return request.post('/v1/auth/logout')
  }
}
