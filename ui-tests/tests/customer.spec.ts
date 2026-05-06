import { test, expect } from '@playwright/test'

/**
 * 客户管理功能测试
 */
test.describe('客户管理', () => {
  
  test.beforeEach(async ({ page }) => {
    // 登录
    await page.goto('/login')
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'admin123')
    await page.click('button:has-text("登录")')
    await page.waitForURL('/')
    
    // 进入客户管理页面
    await page.click('text=基础数据')
    await page.click('text=客户管理')
    await page.waitForURL('/base/customer')
  })

  test('客户列表页面加载成功', async ({ page }) => {
    // 验证页面标题
    await expect(page.locator('.card-header:has-text("客户管理")')).toBeVisible()
    
    // 验证表格存在
    await expect(page.locator('.el-table')).toBeVisible()
  })

  test('新增客户功能', async ({ page }) => {
    // 点击新增按钮
    await page.click('button:has-text("新增客户")')
    
    // 等待对话框出现
    await expect(page.locator('.el-dialog__title:has-text("新增客户")')).toBeVisible()
    
    // 填写客户信息
    await page.fill('input[placeholder="请输入客户编码"]', 'C999')
    await page.fill('input[placeholder="请输入客户名称"]', '测试客户999')
    await page.fill('input[placeholder="请输入联系人"]', '测试联系人')
    await page.fill('input[placeholder="请输入联系电话"]', '13800138999')
    
    // 点击确定
    await page.click('.el-dialog__footer button:has-text("确定")')
    
    // 验证成功提示
    await expect(page.locator('.el-message--success')).toBeVisible()
  })
})
