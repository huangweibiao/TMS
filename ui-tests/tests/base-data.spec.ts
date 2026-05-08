import { test, expect } from '@playwright/test'

test.describe('基础数据模块', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'admin123')
    await page.click('button:has-text("登录")')
    await page.waitForURL(/\/dashboard/)
  })

  test('客户管理页面', async ({ page }) => {
    await page.goto('/base/customer')
    
    await expect(page.locator('h1:has-text("客户管理")')).toBeVisible()
    await expect(page.locator('.el-table')).toBeVisible()
    await expect(page.locator('button:has-text("新增")')).toBeVisible()
  })

  test('新增客户', async ({ page }) => {
    await page.goto('/base/customer')
    
    await page.click('button:has-text("新增")')
    
    await expect(page.locator('.el-dialog__title:has-text("新增客户")')).toBeVisible()
    
    await page.fill('input[placeholder="客户编码"]', 'CUST' + Date.now())
    await page.fill('input[placeholder="客户名称"]', '测试客户')
    await page.fill('input[placeholder="联系人"]', '张三')
    await page.fill('input[placeholder="联系电话"]', '13800138000')
    
    await page.click('button:has-text("确定")')
    
    await expect(page.locator('.el-message--success')).toBeVisible()
  })

  test('车辆管理页面', async ({ page }) => {
    await page.goto('/base/vehicle')
    
    await expect(page.locator('h1:has-text("车辆管理")')).toBeVisible()
    await expect(page.locator('.el-table')).toBeVisible()
  })

  test('司机管理页面', async ({ page }) => {
    await page.goto('/base/driver')
    
    await expect(page.locator('h1:has-text("司机管理")')).toBeVisible()
    await expect(page.locator('.el-table')).toBeVisible()
  })

  test('仓库管理页面', async ({ page }) => {
    await page.goto('/base/warehouse')
    
    await expect(page.locator('h1:has-text("仓库管理")')).toBeVisible()
    await expect(page.locator('.el-table')).toBeVisible()
  })
})
