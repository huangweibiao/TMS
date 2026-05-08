import { test, expect } from '@playwright/test'

test.describe('在途跟踪模块', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'admin123')
    await page.click('button:has-text("登录")')
    await page.waitForURL(/\/dashboard/)
  })

  test('在途跟踪页面显示正确', async ({ page }) => {
    await page.goto('/tracking')
    
    await expect(page.locator('h1:has-text("在途跟踪")')).toBeVisible()
    await expect(page.locator('.tracking-map')).toBeVisible()
  })

  test('查看车辆实时位置', async ({ page }) => {
    await page.goto('/tracking')
    
    await page.waitForSelector('.vehicle-marker', { timeout: 5000 })
    
    const markers = page.locator('.vehicle-marker')
    if (await markers.count() > 0) {
      await markers.first().click()
      
      await expect(page.locator('.vehicle-info-popup')).toBeVisible()
    }
  })

  test('查看轨迹回放', async ({ page }) => {
    await page.goto('/tracking')
    
    await page.waitForSelector('.el-table__row', { timeout: 5000 })
    
    const playbackButton = page.locator('.el-table__row:first-child .el-button:has-text("轨迹")')
    if (await playbackButton.isVisible().catch(() => false)) {
      await playbackButton.click()
      
      await expect(page.locator('.el-dialog__title:has-text("轨迹回放")')).toBeVisible()
    }
  })
})
