import { test, expect } from '@playwright/test'

/**
 * 登录功能测试
 */
test.describe('登录功能', () => {
  
  test('使用正确的用户名密码登录成功', async ({ page }) => {
    // 访问登录页面
    await page.goto('/login')
    
    // 输入用户名和密码
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'admin123')
    
    // 点击登录按钮
    await page.click('button:has-text("登录")')
    
    // 验证登录成功，跳转到首页
    await expect(page).toHaveURL('/')
    await expect(page.locator('text=欢迎使用TMS运输管理系统')).toBeVisible()
  })

  test('使用错误的密码登录失败', async ({ page }) => {
    // 访问登录页面
    await page.goto('/login')
    
    // 输入用户名和错误的密码
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'wrongpassword')
    
    // 点击登录按钮
    await page.click('button:has-text("登录")')
    
    // 验证登录失败，显示错误提示
    await expect(page.locator('.el-message--error')).toBeVisible()
    await expect(page).toHaveURL('/login')
  })

  test('用户名和密码为空时显示验证错误', async ({ page }) => {
    // 访问登录页面
    await page.goto('/login')
    
    // 直接点击登录按钮
    await page.click('button:has-text("登录")')
    
    // 验证表单验证错误
    await expect(page.locator('.el-form-item__error')).toBeVisible()
  })
})
