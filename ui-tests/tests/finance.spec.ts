import { test, expect } from '@playwright/test'

test.describe('财务结算模块', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'admin123')
    await page.click('button:has-text("登录")')
    await page.waitForURL(/\/dashboard/)
  })

  test('费用明细页面显示正确', async ({ page }) => {
    await page.goto('/finance/cost')
    
    await expect(page.locator('h1:has-text("费用明细")')).toBeVisible()
    await expect(page.locator('.el-table')).toBeVisible()
    await expect(page.locator('button:has-text("计算费用")')).toBeVisible()
  })

  test('费用计算功能', async ({ page }) => {
    await page.goto('/finance/cost')
    
    await page.click('button:has-text("计算费用")')
    
    await expect(page.locator('.el-dialog__title:has-text("费用计算")')).toBeVisible()
    
    await page.fill('input[placeholder="运单号"]', 'WB202401010001')
    await page.selectOption('select', '1')
    await page.fill('input[placeholder="单价"]', '2.5')
    
    await page.click('button:has-text("计算")')
    
    await expect(page.locator('.cost-result')).toBeVisible()
  })

  test('结算单页面显示正确', async ({ page }) => {
    await page.goto('/finance/settlement')
    
    await expect(page.locator('h1:has-text("结算管理")')).toBeVisible()
    await expect(page.locator('.el-table')).toBeVisible()
  })

  test('生成结算单功能', async ({ page }) => {
    await page.goto('/finance/settlement')
    
    await page.click('button:has-text("生成结算单")')
    
    await expect(page.locator('.el-dialog__title:has-text("生成结算单")')).toBeVisible()
    
    await page.selectOption('.el-select', '1')
    await page.fill('input[placeholder="开始日期"]', '2024-01-01')
    await page.fill('input[placeholder="结束日期"]', '2024-01-31')
    
    await page.click('button:has-text("生成")')
    
    await expect(page.locator('.el-message--success')).toBeVisible()
  })
})
