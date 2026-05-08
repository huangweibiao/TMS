import { test, expect } from '@playwright/test'

test.describe('报表中心模块', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'admin123')
    await page.click('button:has-text("登录")')
    await page.waitForURL(/\/dashboard/)
  })

  test('报表中心页面显示正确', async ({ page }) => {
    await page.goto('/report')
    
    await expect(page.locator('h1:has-text("报表中心")')).toBeVisible()
    await expect(page.locator('.report-chart')).toBeVisible()
  })

  test('运单统计报表', async ({ page }) => {
    await page.goto('/report')
    
    await page.click('text=运单统计')
    
    await expect(page.locator('.waybill-stat-chart')).toBeVisible()
  })

  test('成本分析报表', async ({ page }) => {
    await page.goto('/report')
    
    await page.click('text=成本分析')
    
    await expect(page.locator('.cost-analysis-chart')).toBeVisible()
  })

  test('导出报表功能', async ({ page }) => {
    await page.goto('/report')
    
    await page.click('button:has-text("导出")')
    
    await expect(page.locator('.el-message--success')).toBeVisible()
  })
})
