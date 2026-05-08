import { test, expect } from '@playwright/test'

test.describe('端到端业务流程测试', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'admin123')
    await page.click('button:has-text("登录")')
    await page.waitForURL(/\/dashboard/)
  })

  test('完整运单生命周期流程', async ({ page }) => {
    // 1. 创建运单
    await page.goto('/waybill')
    await page.click('button:has-text("新增")')
    
    const waybillNo = 'WB' + Date.now()
    await page.fill('input[placeholder="客户订单号"]', 'ORD' + Date.now())
    await page.fill('input[placeholder="发货方名称"]', '北京发货方')
    await page.fill('input[placeholder="发货方电话"]', '13800138000')
    await page.fill('input[placeholder="发货方地址"]', '北京市朝阳区')
    await page.fill('input[placeholder="收货方名称"]', '上海收货方')
    await page.fill('input[placeholder="收货方电话"]', '13900139000')
    await page.fill('input[placeholder="收货方地址"]', '上海市浦东新区')
    await page.fill('input[placeholder="总重量"]', '2000')
    await page.fill('input[placeholder="总体积"]', '10')
    
    await page.click('button:has-text("确定")')
    await expect(page.locator('.el-message--success')).toBeVisible()

    // 2. 运单调度
    await page.goto('/dispatch')
    await page.click('button:has-text("智能调度")')
    
    await page.waitForSelector('.dispatch-plan-item', { timeout: 5000 })
    const planItems = page.locator('.dispatch-plan-item')
    
    if (await planItems.count() > 0) {
      await planItems.first().locator('button:has-text("确认")').click()
      await expect(page.locator('.el-message--success')).toBeVisible()
    }

    // 3. 确认发车
    await page.goto('/dispatch')
    await page.waitForSelector('.el-table__row', { timeout: 5000 })
    
    const startButton = page.locator('.el-table__row:first-child .el-button:has-text("发车")')
    if (await startButton.isVisible().catch(() => false)) {
      await startButton.click()
      await page.click('button:has-text("确定")')
      await expect(page.locator('.el-message--success')).toBeVisible()
    }

    // 4. 费用计算
    await page.goto('/finance/cost')
    await page.click('button:has-text("计算费用")')
    
    await expect(page.locator('.el-dialog__title:has-text("费用计算")')).toBeVisible()
    await page.fill('input[placeholder="运单号"]', waybillNo)
    await page.selectOption('select', '1')
    await page.fill('input[placeholder="单价"]', '2.5')
    await page.click('button:has-text("计算")')
    
    await expect(page.locator('.cost-result')).toBeVisible()
  })

  test('客户-运单-结算完整流程', async ({ page }) => {
    // 1. 创建客户
    await page.goto('/base/customer')
    await page.click('button:has-text("新增")')
    
    const customerCode = 'CUST' + Date.now()
    await page.fill('input[placeholder="客户编码"]', customerCode)
    await page.fill('input[placeholder="客户名称"]', '测试客户' + Date.now())
    await page.fill('input[placeholder="联系人"]', '张三')
    await page.fill('input[placeholder="联系电话"]', '13800138000')
    await page.click('button:has-text("确定")')
    
    await expect(page.locator('.el-message--success')).toBeVisible()

    // 2. 创建运单
    await page.goto('/waybill')
    await page.click('button:has-text("新增")')
    
    await page.fill('input[placeholder="客户订单号"]', 'ORD' + Date.now())
    await page.fill('input[placeholder="发货方名称"]', '发货方测试')
    await page.fill('input[placeholder="发货方电话"]', '13800138000')
    await page.fill('input[placeholder="发货方地址"]', '北京市朝阳区')
    await page.fill('input[placeholder="收货方名称"]', '收货方测试')
    await page.fill('input[placeholder="收货方电话"]', '13900139000')
    await page.fill('input[placeholder="收货方地址"]', '上海市浦东新区')
    await page.fill('input[placeholder="总重量"]', '1000')
    await page.fill('input[placeholder="总体积"]', '5')
    
    await page.click('button:has-text("确定")')
    await expect(page.locator('.el-message--success')).toBeVisible()

    // 3. 生成结算单
    await page.goto('/finance/settlement')
    await page.click('button:has-text("生成结算单")')
    
    await expect(page.locator('.el-dialog__title:has-text("生成结算单")')).toBeVisible()
    await page.selectOption('.el-select', '1')
    await page.fill('input[placeholder="开始日期"]', '2024-01-01')
    await page.fill('input[placeholder="结束日期"]', '2024-12-31')
    await page.click('button:has-text("生成")')
    
    await expect(page.locator('.el-message--success')).toBeVisible()
  })

  test('异常处理流程', async ({ page }) => {
    // 1. 创建运单
    await page.goto('/waybill')
    await page.click('button:has-text("新增")')
    
    await page.fill('input[placeholder="客户订单号"]', 'ORD' + Date.now())
    await page.fill('input[placeholder="发货方名称"]', '异常测试发货方')
    await page.fill('input[placeholder="发货方电话"]', '13800138000')
    await page.fill('input[placeholder="发货方地址"]', '北京市朝阳区')
    await page.fill('input[placeholder="收货方名称"]', '异常测试收货方')
    await page.fill('input[placeholder="收货方电话"]', '13900139000')
    await page.fill('input[placeholder="收货方地址"]', '上海市浦东新区')
    await page.fill('input[placeholder="总重量"]', '1000')
    await page.fill('input[placeholder="总体积"]', '5')
    
    await page.click('button:has-text("确定")')
    await expect(page.locator('.el-message--success')).toBeVisible()

    // 2. 标记异常
    await page.goto('/waybill')
    await page.waitForSelector('.el-table__row', { timeout: 5000 })
    
    const exceptionButton = page.locator('.el-table__row:first-child .el-button:has-text("异常")')
    if (await exceptionButton.isVisible().catch(() => false)) {
      await exceptionButton.click()
      
      await expect(page.locator('.el-dialog__title:has-text("标记异常")')).toBeVisible()
      await page.selectOption('.el-select', '1')
      await page.fill('textarea[placeholder="异常描述"]', '测试异常')
      await page.click('button:has-text("确定")')
      
      await expect(page.locator('.el-message--success')).toBeVisible()
    }

    // 3. 查看在途跟踪
    await page.goto('/tracking')
    await expect(page.locator('h1:has-text("在途跟踪")')).toBeVisible()
  })
})
