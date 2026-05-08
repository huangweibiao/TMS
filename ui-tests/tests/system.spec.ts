import { test, expect } from '@playwright/test'

test.describe('系统管理模块', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'admin123')
    await page.click('button:has-text("登录")')
    await page.waitForURL(/\/dashboard/)
  })

  test('用户管理页面', async ({ page }) => {
    await page.goto('/system/user')
    
    await expect(page.locator('h1:has-text("用户管理")')).toBeVisible()
    await expect(page.locator('.el-table')).toBeVisible()
  })

  test('角色管理页面', async ({ page }) => {
    await page.goto('/system/role')
    
    await expect(page.locator('h1:has-text("角色管理")')).toBeVisible()
    await expect(page.locator('.el-table')).toBeVisible()
  })

  test('操作日志页面', async ({ page }) => {
    await page.goto('/system/log')
    
    await expect(page.locator('h1:has-text("操作日志")')).toBeVisible()
    await expect(page.locator('.el-table')).toBeVisible()
  })
})
