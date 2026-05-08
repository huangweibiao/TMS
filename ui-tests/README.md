# TMS UI E2E 自动化测试

基于 Playwright 的端到端 UI 自动化测试项目。

## 项目结构

```
ui-tests/
├── tests/                    # 测试用例目录
│   ├── auth.spec.ts         # 认证模块测试
│   ├── dashboard.spec.ts    # 仪表盘测试
│   ├── waybill.spec.ts      # 运单管理测试
│   ├── dispatch.spec.ts     # 调度管理测试
│   ├── finance.spec.ts      # 财务结算测试
│   ├── base-data.spec.ts    # 基础数据测试
│   ├── system.spec.ts       # 系统管理测试
│   ├── tracking.spec.ts     # 在途跟踪测试
│   ├── report.spec.ts       # 报表中心测试
│   └── end-to-end.spec.ts   # 端到端业务流程测试
├── playwright.config.ts     # Playwright 配置文件
├── package.json             # 项目依赖
└── README.md               # 项目说明
```

## 安装依赖

```bash
cd /Users/huangweibiao/AiProject/TMS/ui-tests
npm install
npx playwright install chromium
```

## 运行测试

```bash
# 运行所有测试
npm run test

# 运行测试并显示浏览器界面
npm run test:headed

# 运行测试并打开 UI 模式
npm run test:ui

# 调试模式运行
npm run test:debug

# 查看测试报告
npm run test:report
```

## 测试覆盖模块

### 1. 认证模块 (auth.spec.ts)
- 登录页面显示
- 错误凭据处理
- 成功登录跳转
- 未登录访问保护

### 2. 运单管理 (waybill.spec.ts)
- 运单列表显示
- 创建运单流程
- 搜索运单功能
- 运单详情查看
- 运单状态变更

### 3. 调度管理 (dispatch.spec.ts)
- 调度列表显示
- 智能调度功能
- 调度发车操作

### 4. 财务结算 (finance.spec.ts)
- 费用明细页面
- 费用计算功能
- 结算单管理
- 生成结算单

### 5. 基础数据 (base-data.spec.ts)
- 客户管理
- 车辆管理
- 司机管理
- 仓库管理

### 6. 系统管理 (system.spec.ts)
- 用户管理
- 角色管理
- 操作日志

### 7. 在途跟踪 (tracking.spec.ts)
- 实时位置查看
- 轨迹回放

### 8. 报表中心 (report.spec.ts)
- 运单统计
- 成本分析
- 报表导出

### 9. 端到端流程 (end-to-end.spec.ts)
- 完整运单生命周期
- 客户-运单-结算流程
- 异常处理流程

## 配置说明

### 基础 URL 配置
在 `playwright.config.ts` 中修改 `baseURL`：

```typescript
use: {
  baseURL: 'http://localhost:5173',  // 开发环境
  // baseURL: 'http://localhost:8080', // 生产环境
}
```

### 测试账号配置
测试使用默认账号：
- 用户名: admin
- 密码: admin123

可在测试文件中修改登录凭据。

## 编写新测试

参考现有测试文件，基本结构如下：

```typescript
import { test, expect } from '@playwright/test'

test.describe('模块名称', () => {
  test.beforeEach(async ({ page }) => {
    // 登录
    await page.goto('/login')
    await page.fill('input[placeholder="请输入用户名"]', 'admin')
    await page.fill('input[placeholder="请输入密码"]', 'admin123')
    await page.click('button:has-text("登录")')
    await page.waitForURL(/\/dashboard/)
  })

  test('测试用例描述', async ({ page }) => {
    // 测试步骤
    await page.goto('/module-path')
    
    // 断言
    await expect(page.locator('selector')).toBeVisible()
  })
})
```

## 注意事项

1. 运行测试前确保前端服务已启动
2. 测试数据会根据时间戳动态生成，避免冲突
3. 部分测试会根据页面元素是否存在进行条件判断
4. 测试失败时会自动截图和录制视频
