import { test, expect } from '@playwright/test'

test.describe('调度管理模块', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'admin123')
    await page.click('button:has-text("登录")')
    await page.waitForURL(/\/dashboard/)
  })

  test('调度列表页面显示正确', async ({ page }) => {
    await page.goto('/dispatch')
    
    await expect(page.locator('h1:has-text("调度管理")')).toBeVisible()
    await expect(page.locator('.el-table')).toBeVisible()
    await expect(page.locator('button:has-text("智能调度")')).toBeVisible()
  })

  test('智能调度功能', async ({ page }) => {
    await page.goto('/dispatch')
    
    await page.click('button:has-text("智能调度")')
    
    await expect(page.locator('.el-dialog__title:has-text("智能调度")')).toBeVisible()
    
    await page.waitForSelector('.dispatch-plan-item', { timeout: 5000 })
    
    const planItems = page.locator('.dispatch-plan-item')
    if (await planItems.count() > 0) {
      await planItems.first().locator('button:has-text("确认")').click()
      await expect(page.locator('.el-message--success')).toBeVisible()
    }
  })

  test('调度发车操作', async ({ page }) => {
    await page.goto('/dispatch')
    
    await page.waitForSelector('.el-table__row', { timeout: 5000 })
    
    const startButton = page.locator('.el-table__row:first-child .el-button:has-text("发车")')
    if (await startButton.isVisible().catch(() => false)) {
      await startButton.click()
      
      await page.click('button:has-text("确定")')
      
      await expect(page.locator('.el-message--success')).toBeVisible()
    }
  })
})
