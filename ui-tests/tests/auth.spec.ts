import { test, expect } from '@playwright/test'

test.describe('认证模块', () => {
  test('登录页面显示正确', async ({ page }) => {
    await page.goto('/login')
    
    // 验证页面标题
    await expect(page).toHaveTitle(/TMS/)
    
    // 验证登录表单元素
    await expect(page.locator('input[placeholder="请输入用户名"]')).toBeVisible()
    await expect(page.locator('input[placeholder="请输入密码"]')).toBeVisible()
    await expect(page.locator('button:has-text("登录")')).toBeVisible()
  })

  test('用户名密码错误时显示错误信息', async ({ page }) => {
    await page.goto('/login')
    
    // 输入错误的凭据
    await page.fill('input[placeholder="请输入用户名"]', 'wronguser')
    await page.fill('input[placeholder="请输入密码"]', 'wrongpass')
    await page.click('button:has-text("登录")')
    
    // 验证错误提示
    await expect(page.locator('.el-message--error')).toBeVisible()
  })

  test('成功登录后跳转到首页', async ({ page }) => {
    await page.goto('/login')
    
    // 输入正确的凭据（假设存在测试账号）
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'admin123')
    await page.click('button:has-text("登录")')
    
    // 验证跳转到首页
    await expect(page).toHaveURL(/\/dashboard/)
    await expect(page.locator('.dashboard-container')).toBeVisible()
  })

  test('未登录用户访问受保护页面重定向到登录页', async ({ page }) => {
    await page.goto('/waybill')
    
    // 验证重定向到登录页
    await expect(page).toHaveURL(/\/login/)
  })
})
