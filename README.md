# TMS运输管理系统

基于Spring Boot 3 + Vue 3的运输管理系统，支持运输订单全生命周期管理、运力调度、在途监控、财务结算等功能。

## 技术栈

### 后端
- Spring Boot 3.5.x
- Java 21
- Spring Data JPA
- Spring Security + JWT
- MySQL 8
- Maven

### 前端
- Vue 3
- TypeScript
- Element Plus
- Pinia
- Vue Router
- Axios

### 测试
- JUnit 5 + Mockito（后端单元测试）
- Playwright（前端E2E测试）

## 项目结构

```
TMS/
├── backend/              # 后端代码
│   ├── src/
│   │   ├── main/java/com/tms/    # Java源代码
│   │   └── main/resources/       # 配置文件
│   └── pom.xml
├── frontend/             # 前端代码
│   ├── src/              # Vue源代码
│   └── package.json
├── ui-tests/             # Playwright E2E测试
├── build.sh              # 打包脚本
└── README.md
```

## 快速开始

### 环境要求
- JDK 21
- Node.js 18+
- MySQL 8
- Maven 3.8+

### 1. 克隆项目

```bash
git clone <repository-url>
cd TMS
```

### 2. 数据库配置

编辑 `backend/src/main/resources/application-dev.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tms_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: your_password
```

### 3. 打包部署

使用打包脚本一键构建：

```bash
./build.sh
```

打包完成后会生成 `tms-backend.jar` 文件。

### 4. 启动应用

```bash
java -jar tms-backend.jar
```

应用启动后访问：http://localhost:8080

默认登录账号：
- 用户名：admin
- 密码：admin123

## 开发模式

### 后端开发

```bash
cd backend
mvn spring-boot:run
```

### 前端开发

```bash
cd frontend
npm install
npm run dev
```

前端开发服务器运行在 http://localhost:3000，会自动代理到后端API。

## 功能模块

- **基础数据**：客户管理、承运商管理、车辆管理、司机管理、仓库管理
- **订单管理**：运单创建、审核、状态跟踪
- **调度管理**：智能派车、调度单管理、装货管理
- **在途监控**：GPS轨迹、异常事件、温湿度监控
- **财务结算**：费用计算、结算单管理、回单管理
- **系统管理**：用户管理、角色管理、权限管理、操作日志

## 数据库设计

数据库表名以 `tms_` 为前缀，主要表包括：
- tms_user, tms_role, tms_permission - 用户权限
- tms_customer, tms_carrier, tms_vehicle, tms_driver, tms_warehouse - 基础数据
- tms_waybill, tms_waybill_detail - 订单
- tms_dispatch, tms_loading_order - 调度
- tms_track_point, tms_onway_event, tms_temp_humidity - 在途监控
- tms_cost_detail, tms_settlement, tms_receipt - 财务结算

## 测试

### 后端单元测试

```bash
cd backend
mvn test
```

### 前端E2E测试

```bash
cd ui-tests
npm install
npx playwright install
npm test
```

## 部署说明

### 生产环境配置

1. 修改 `application-prod.yml` 中的数据库配置
2. 设置环境变量 `DB_USERNAME` 和 `DB_PASSWORD`
3. 使用生产模式启动：

```bash
java -jar tms-backend.jar --spring.profiles.active=prod
```

## 许可证

MIT License
