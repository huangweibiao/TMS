import { test, expect } from '@playwright/test'

test.describe('仪表盘模块', () => {
  test.beforeEach(async ({ page }) => {
    // 登录
    await page.goto('/login')
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'admin123')
    await page.click('button:has-text("登录")')
    await page.waitForURL(/\/dashboard/)
  })

  test('仪表盘页面显示正确', async ({ page }) => {
    // 验证页面标题
    await expect(page.locator('h1:has-text("Dashboard")')).toBeVisible()
    
    // 验证统计卡片
    await expect(page.locator('.stat-card')).toHaveCount.greaterThanOrEqual(4)
    
    // 验证图表区域
    await expect(page.locator('.chart-container')).toBeVisible()
  })

  test('快捷操作按钮可用', async ({ page }) => {
    // 验证快捷操作按钮
    await expect(page.locator('button:has-text("新建运单")')).toBeVisible()
    await expect(page.locator('button:has-text("智能调度")')).toBeVisible()
    await expect(page.locator('button:has-text("查看报表")')).toBeVisible()
  })

  test('点击新建运单跳转到运单页面', async ({ page }) => {
    await page.click('button:has-text("新建运单")')
    await expect(page).toHaveURL(/\/waybill/)
  })
})
