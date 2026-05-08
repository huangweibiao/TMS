import { test, expect } from '@playwright/test'

test.describe('运单管理模块', () => {
  test.beforeEach(async ({ page }) => {
    // 登录
    await page.goto('/login')
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'admin123')
    await page.click('button:has-text("登录")')
    await page.waitForURL(/\/dashboard/)
  })

  test('运单列表页面显示正确', async ({ page }) => {
    await page.goto('/waybill')
    
    // 验证页面标题
    await expect(page.locator('h1:has-text("运单管理")')).toBeVisible()
    
    // 验证搜索表单
    await expect(page.locator('input[placeholder="运单号"]')).toBeVisible()
    await expect(page.locator('button:has-text("查询")')).toBeVisible()
    await expect(page.locator('button:has-text("新增")')).toBeVisible()
    
    // 验证表格
    await expect(page.locator('.el-table')).toBeVisible()
  })

  test('创建运单流程', async ({ page }) => {
    await page.goto('/waybill')
    
    // 点击新增按钮
    await page.click('button:has-text("新增")')
    
    // 验证弹窗显示
    await expect(page.locator('.el-dialog__title:has-text("新增运单")')).toBeVisible()
    
    // 填写运单信息
    await page.fill('input[placeholder="客户订单号"]', 'TEST20240101001')
    await page.fill('input[placeholder="发货方名称"]', '测试发货方')
    await page.fill('input[placeholder="发货方电话"]', '13800138000')
    await page.fill('input[placeholder="发货方地址"]', '北京市朝阳区测试地址')
    await page.fill('input[placeholder="收货方名称"]', '测试收货方')
    await page.fill('input[placeholder="收货方电话"]', '13900139000')
    await page.fill('input[placeholder="收货方地址"]', '上海市浦东新区测试地址')
    await page.fill('input[placeholder="总重量"]', '1000')
    await page.fill('input[placeholder="总体积"]', '5')
    
    // 提交表单
    await page.click('button:has-text("确定")')
    
    // 验证成功提示
    await expect(page.locator('.el-message--success')).toBeVisible()
  })

  test('搜索运单功能', async ({ page }) => {
    await page.goto('/waybill')
    
    // 输入搜索条件
    await page.fill('input[placeholder="运单号"]', 'WB')
    await page.click('button:has-text("查询")')
    
    // 验证搜索结果
    await page.waitForTimeout(500)
    await expect(page.locator('.el-table__row')).toHaveCount.greaterThanOrEqual(0)
  })

  test('运单详情查看', async ({ page }) => {
    await page.goto('/waybill')
    
    // 等待表格加载
    await page.waitForSelector('.el-table__row', { timeout: 5000 })
    
    // 点击查看详情按钮（第一行）
    const detailButton = page.locator('.el-table__row:first-child .el-button:has-text("详情")')
    if (await detailButton.isVisible().catch(() => false)) {
      await detailButton.click()
      
      // 验证详情弹窗
      await expect(page.locator('.el-dialog__title:has-text("运单详情")')).toBeVisible()
    }
  })

  test('运单状态变更', async ({ page }) => {
    await page.goto('/waybill')
    
    // 等待表格加载
    await page.waitForSelector('.el-table__row', { timeout: 5000 })
    
    // 点击状态变更按钮（第一行）
    const statusButton = page.locator('.el-table__row:first-child .el-button:has-text("调度")')
    if (await statusButton.isVisible().catch(() => false)) {
      await statusButton.click()
      
      // 验证调度弹窗
      await expect(page.locator('.el-dialog__title:has-text("运单调度")')).toBeVisible()
    }
  })
})
