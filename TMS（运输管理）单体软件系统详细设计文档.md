# TMS（运输管理）单体软件系统详细设计文档

## 一、系统目标与范围

### 1.1 目标

构建一个支持运输订单全生命周期管理、运力智能调度、路线规划优化、在途实时跟踪、费用结算管理、多维度报表分析的一体化TMS系统，满足中大型企业初期或统一部署场景的运输管理需求。

### 1.2 用户角色

调度员、客服/运营、财务、司机、管理层。

## 二、系统架构设计

### 2.1 总体架构

采用**分层单体架构（Modular Monolith）**，兼顾业务解耦与部署便捷性，为未来拆分微服务预留扩展空间：

```Plain Text

┌─────────────────────────────────────────────────────────┐
│                    前端层 (Web/App)                      │
├─────────────────────────────────────────────────────────┤
│                    API网关层 (Spring Boot)               │
├─────────────────────────────────────────────────────────┤
│  业务逻辑层（按域拆分模块）                                │
│  ┌──────┬──────┬──────┬──────┬──────┬──────┬──────┐      │
│  │订单  │调度  │运输  │在途  │财务  │基础  │报表  │      │
│  │模块  │模块  │执行  │监控  │结算  │数据  │模块  │      │
│  └──────┴──────┴──────┴──────┴──────┴──────┴──────┘      │
├─────────────────────────────────────────────────────────┤
│  基础设施层                                               │
│  ┌──────┬──────┬──────┬────────────────────────────┐     │
│  │MySQL │Redis │MQ    │Repository/外部系统集成      │     │
│  │(主从)│(集群)│(集群)│(权限/日志/第三方接口)       │     │
│  └──────┴──────┴──────┴────────────────────────────┘     │
└─────────────────────────────────────────────────────────┘
```

### 2.2 模块划分

|模块|功能职责|
|---|---|
|订单模块|订单创建、审核、拆分、合并、取消、全生命周期状态管理|
|调度模块|智能配载、路径规划、派车/司机分配、手动/自动派单、调度规则引擎|
|运输执行|装货、发车、到达、签收、异常处理（延误/破损/退货）|
|在途监控|GPS轨迹采集/回放、温湿度监控、异常报警、ETA预测|
|财务结算|运费计算（按重量/体积/里程/阶梯价）、对账、发票管理、成本核算、结算单管理|
|基础数据|客户、承运商、车辆、司机、仓库、产品、地址库等基础信息管理|
|报表中心|运单报表、KPI看板、运输时效、成本分析、车辆利用率、异常统计|
|权限/系统|RBAC权限管理、操作日志、系统配置、外部接口集成|
### 2.3 部署架构

```Plain Text

                    ┌─────────────┐
                    │   Nginx     │
                    │  (负载均衡)  │
                    └──────┬──────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
         ┌────▼───┐   ┌────▼───┐   ┌────▼───┐
         │ App-1  │   │ App-2  │   │ App-3  │
         │(Spring)│   │(Spring)│   │(Spring)│
         └────┬───┘   └────┬───┘   └────┬───┘
              │            │            │
              └────────────┼────────────┘
                           │
         ┌─────────────────┼─────────────────┐
         │                 │                 │
    ┌────▼────┐       ┌─────▼─────┐      ┌────▼────┐
    │  MySQL  │       │   Redis   │      │RabbitMQ │
    │ (主从)  │       │ (集群)    │      │ (集群)  │
    └─────────┘       └───────────┘      └─────────┘
```

## 三、核心业务设计

### 3.1 订单模块

#### 3.1.1 状态机

```Plain Text

CREATED(待调度) → ASSIGNED(已调度) → PICKED(提货中) → IN_TRANSIT(运输中) → DELIVERED(签收) → COMPLETED(完成)
                  ↘ CANCELLED(取消)        ↘ EXCEPTION(异常) → 处理后可回流至任意前序状态
```

#### 3.1.2 核心流程

运单创建 → 审核 → 调度分配 → 提货 → 运输 → 签收 → 完成/异常处理  

（支持订单拆分、合并、取消，异常可退回任意环节）

### 3.2 调度模块

#### 3.2.1 核心策略

- 基础策略：距离优先、成本优先、时效优先

- 进阶策略：VRP（车辆路径优化）、AI智能匹配（后期扩展）

- 简化算法（初版）：贪心算法、最近车辆匹配

#### 3.2.2 核心流程

运力匹配（车/司机/承运商）→ 路径规划 → 派单确认 → 发车执行 → 运输跟踪

### 3.3 结算模块

#### 3.3.1 计费模型

- 按重量计费：总重量 × 单位重量运价

- 按体积计费：总体积 × 单位体积运价

- 按里程计费：运输里程 × 单位里程运价

- 阶梯价格：按重量/体积/里程区间设置不同单价

- 附加费用：装卸费、保险费、等待费、高速费、油费、罚款等

## 四、数据库设计

### 4.1 设计原则

- 强一致性事务（Spring @Transactional）

- 合理索引（订单ID、状态、时间、外键等高频查询字段）

- 分库分表预留（按订单号/时间分片）

- 字段设计兼顾业务场景与扩展（如温控、危险品标识）

### 4.2 核心实体关系图

```Plain Text

客户 ──┬── 运单 ──┬── 调度单 ──┬── 车辆
承运商 ┘          │            ├── 司机
                  │            └── 轨迹点
                  ├── 运单明细
                  ├── 在途事件
                  ├── 温湿度记录
                  ├── 费用明细 ──┬── 结算单
                  └── 回单       └── 客户/承运商
```

### 4.3 数据表详细设计

#### 4.3.1 基础数据表

##### 1. 客户表 (t_customer)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|customer_code|varchar|32|N|-|客户编码，唯一索引|
|customer_name|varchar|100|N|-|客户名称|
|short_name|varchar|50|Y|NULL|简称|
|contact_person|varchar|50|Y|NULL|联系人|
|contact_phone|varchar|20|Y|NULL|联系电话|
|contact_address|varchar|200|Y|NULL|联系地址|
|credit_level|tinyint|-|Y|1|信用等级:1-A,2-B,3-C|
|settlement_cycle|tinyint|-|Y|1|结算周期:1-月结,2-周结,3-现结|
|status|tinyint|-|N|1|状态:1-启用,0-停用|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
|update_time|datetime|-|N|CURRENT_TIMESTAMP ON UPDATE|更新时间|
##### 2. 承运商表 (t_carrier)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|carrier_code|varchar|32|N|-|承运商编码，唯一索引|
|carrier_name|varchar|100|N|-|承运商名称|
|carrier_type|tinyint|-|Y|1|类型:1-自有,2-外协,3-专线|
|license_no|varchar|50|Y|NULL|营业执照号|
|tax_no|varchar|50|Y|NULL|税号|
|contact_person|varchar|50|Y|NULL|联系人|
|contact_phone|varchar|20|Y|NULL|联系电话|
|cooperation_start|date|-|Y|NULL|合作开始日期|
|cooperation_end|date|-|Y|NULL|合作结束日期|
|rating|decimal|(3,2)|Y|5.00|评分(1-5)|
|status|tinyint|-|N|1|状态:1-启用,0-停用|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
|update_time|datetime|-|N|CURRENT_TIMESTAMP ON UPDATE|更新时间|
##### 3. 车辆表 (t_vehicle)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|plate_number|varchar|20|N|-|车牌号，唯一索引|
|vehicle_type|varchar|30|Y|NULL|车型:厢式/平板/高栏/冷藏|
|vehicle_brand|varchar|50|Y|NULL|品牌|
|load_capacity|decimal|(10,2)|Y|NULL|载重(吨)|
|volume_capacity|decimal|(10,2)|Y|NULL|容积(立方米)|
|length|decimal|(8,2)|Y|NULL|车长(米)|
|width|decimal|(8,2)|Y|NULL|车宽(米)|
|height|decimal|(8,2)|Y|NULL|车高(米)|
|owner_type|tinyint|-|Y|1|所属:1-自有,2-外协|
|carrier_id|bigint|-|Y|NULL|承运商ID，外键|
|gps_device_id|varchar|50|Y|NULL|GPS设备ID|
|status|tinyint|-|N|1|状态:1-可用,2-维修,3-报废|
|last_maintenance|date|-|Y|NULL|最近保养日期|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
|update_time|datetime|-|N|CURRENT_TIMESTAMP ON UPDATE|更新时间|
##### 4. 司机表 (t_driver)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|driver_name|varchar|50|N|-|司机姓名|
|id_card|varchar|18|N|-|身份证号，唯一索引|
|phone|varchar|20|N|-|手机号|
|license_type|varchar|10|Y|NULL|驾照类型:A1/A2/B1/B2|
|license_no|varchar|30|Y|NULL|驾驶证号|
|hire_date|date|-|Y|NULL|入职日期|
|carrier_id|bigint|-|Y|NULL|所属承运商ID|
|vehicle_id|bigint|-|Y|NULL|当前绑定车辆ID|
|status|tinyint|-|N|1|状态:1-空闲,2-在途,3-休息,4-离职|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
|update_time|datetime|-|N|CURRENT_TIMESTAMP ON UPDATE|更新时间|
##### 5. 仓库表 (t_warehouse)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|warehouse_code|varchar|32|N|-|仓库编码，唯一索引|
|warehouse_name|varchar|100|N|-|仓库名称|
|warehouse_type|tinyint|-|Y|1|类型:1-发货仓,2-中转仓,3-收货仓|
|province|varchar|50|Y|NULL|省|
|city|varchar|50|Y|NULL|市|
|district|varchar|50|Y|NULL|区|
|address|varchar|200|Y|NULL|详细地址|
|longitude|decimal|(10,7)|Y|NULL|经度|
|latitude|decimal|(10,7)|Y|NULL|纬度|
|contact_person|varchar|50|Y|NULL|联系人|
|contact_phone|varchar|20|Y|NULL|联系电话|
|status|tinyint|-|N|1|状态:1-启用,0-停用|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
|update_time|datetime|-|N|CURRENT_TIMESTAMP ON UPDATE|更新时间|
#### 4.3.2 订单模块表

##### 6. 运单主表 (t_waybill)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|waybill_no|varchar|32|N|-|运单号，唯一索引|
|order_no|varchar|32|Y|NULL|客户订单号|
|customer_id|bigint|-|N|-|客户ID，外键|
|carrier_id|bigint|-|Y|NULL|承运商ID|
|product_type|varchar|50|Y|NULL|货物类型|
|total_weight|decimal|(12,3)|Y|NULL|总重量(kg)|
|total_volume|decimal|(12,3)|Y|NULL|总体积(m³)|
|total_quantity|int|-|Y|NULL|总件数|
|goods_value|decimal|(14,2)|Y|NULL|货物价值(元)|
|is_hazardous|tinyint|-|Y|0|是否危险品:1-是,0-否|
|is_fragile|tinyint|-|Y|0|是否易碎:1-是,0-否|
|need_temperature|tinyint|-|Y|0|是否需要温控:1-是,0-否|
|min_temp|decimal|(5,1)|Y|NULL|最低温度(℃)|
|max_temp|decimal|(5,1)|Y|NULL|最高温度(℃)|
|shipper_name|varchar|100|N|-|发货方名称|
|shipper_phone|varchar|20|N|-|发货方电话|
|shipper_address|varchar|200|N|-|发货方地址|
|consignee_name|varchar|100|N|-|收货方名称|
|consignee_phone|varchar|20|N|-|收货方电话|
|consignee_address|varchar|200|N|-|收货方地址|
|expect_pickup_time|datetime|-|Y|NULL|期望提货时间|
|expect_delivery_time|datetime|-|Y|NULL|期望送达时间|
|actual_pickup_time|datetime|-|Y|NULL|实际提货时间|
|actual_delivery_time|datetime|-|Y|NULL|实际送达时间|
|waybill_status|tinyint|-|N|1|状态:1-待调度,2-已调度,3-提货中,4-运输中,5-签收,6-异常,7-取消|
|remark|varchar|500|Y|NULL|备注|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
|create_by|varchar|50|Y|NULL|创建人|
|update_time|datetime|-|N|CURRENT_TIMESTAMP ON UPDATE|更新时间|
##### 7. 运单明细表 (t_waybill_detail)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|waybill_id|bigint|-|N|-|运单ID，外键|
|sku_code|varchar|50|Y|NULL|SKU编码|
|sku_name|varchar|200|Y|NULL|商品名称|
|quantity|int|-|N|-|数量|
|unit|varchar|10|Y|个|单位|
|unit_weight|decimal|(10,3)|Y|NULL|单重(kg)|
|unit_volume|decimal|(10,3)|Y|NULL|单体积(m³)|
|unit_price|decimal|(12,2)|Y|NULL|单价(元)|
|total_price|decimal|(14,2)|Y|NULL|总价(元)|
|remark|varchar|200|Y|NULL|备注|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
|update_time|datetime|-|N|CURRENT_TIMESTAMP ON UPDATE|更新时间|
#### 4.3.3 调度模块表

##### 8. 调度单表 (t_dispatch)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|dispatch_no|varchar|32|N|-|调度单号，唯一索引|
|waybill_id|bigint|-|N|-|运单ID，外键|
|vehicle_id|bigint|-|N|-|车辆ID，外键|
|driver_id|bigint|-|N|-|司机ID，外键|
|carrier_id|bigint|-|Y|NULL|承运商ID|
|route_plan|text|-|Y|NULL|路径规划(JSON)|
|planned_distance|decimal|(10,2)|Y|NULL|计划里程(km)|
|actual_distance|decimal|(10,2)|Y|NULL|实际里程(km)|
|planned_start_time|datetime|-|Y|NULL|计划发车时间|
|planned_end_time|datetime|-|Y|NULL|计划到达时间|
|actual_start_time|datetime|-|Y|NULL|实际发车时间|
|actual_end_time|datetime|-|Y|NULL|实际到达时间|
|dispatch_status|tinyint|-|N|1|状态:1-待发车,2-已发车,3-已完成,4-已取消|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
|create_by|varchar|50|Y|NULL|创建人|
|update_time|datetime|-|N|CURRENT_TIMESTAMP ON UPDATE|更新时间|
##### 9. 装货单表 (t_loading_order)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|loading_no|varchar|32|N|-|装货单号，唯一索引|
|dispatch_id|bigint|-|N|-|调度单ID，外键|
|warehouse_id|bigint|-|N|-|仓库ID，外键|
|loading_time|datetime|-|Y|NULL|装货时间|
|operator|varchar|50|Y|NULL|操作人|
|status|tinyint|-|N|1|状态:1-待装货,2-装货中,3-已完成|
|remark|varchar|200|Y|NULL|备注|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
|update_time|datetime|-|N|CURRENT_TIMESTAMP ON UPDATE|更新时间|
#### 4.3.4 在途监控表

##### 10. 轨迹点表 (t_track_point)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|dispatch_id|bigint|-|N|-|调度单ID，索引|
|vehicle_id|bigint|-|N|-|车辆ID，索引|
|longitude|decimal|(10,7)|N|-|经度|
|latitude|decimal|(10,7)|N|-|纬度|
|speed|decimal|(6,2)|Y|NULL|速度(km/h)|
|direction|int|-|Y|NULL|方向角度|
|location_time|datetime|-|N|-|定位时间|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
##### 11. 在途事件表 (t_onway_event)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|dispatch_id|bigint|-|N|-|调度单ID|
|waybill_id|bigint|-|N|-|运单ID|
|event_type|tinyint|-|N|-|类型:1-超速,2-疲劳驾驶,3-偏离路线,4-长时间停留,5-温度异常,6-紧急报警|
|event_level|tinyint|-|N|-|级别:1-提示,2-警告,3-严重|
|event_content|varchar|500|Y|NULL|事件内容|
|location|varchar|200|Y|NULL|发生位置|
|event_time|datetime|-|N|-|事件时间|
|is_handled|tinyint|-|Y|0|是否处理:1-是,0-否|
|handle_result|varchar|200|Y|NULL|处理结果|
|handle_time|datetime|-|Y|NULL|处理时间|
|handle_by|varchar|50|Y|NULL|处理人|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
|update_time|datetime|-|N|CURRENT_TIMESTAMP ON UPDATE|更新时间|
##### 12. 温湿度记录表 (t_temp_humidity)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|dispatch_id|bigint|-|N|-|调度单ID|
|device_id|varchar|50|N|-|设备ID|
|temperature|decimal|(5,2)|N|-|温度(℃)|
|humidity|decimal|(5,2)|N|-|湿度(%)|
|record_time|datetime|-|N|-|记录时间|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
#### 4.3.5 财务结算表

##### 13. 费用明细表 (t_cost_detail)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|waybill_id|bigint|-|N|-|运单ID|
|cost_type|tinyint|-|N|-|费用类型:1-运费,2-装卸费,3-保险费,4-等待费,5-高速费,6-油费,7-罚款|
|amount|decimal|(14,2)|N|-|金额(元)|
|currency|varchar|3|Y|CNY|币种|
|direction|tinyint|-|N|-|方向:1-应收(客户),2-应付(承运商)|
|settlement_status|tinyint|-|Y|1|结算状态:1-未结算,2-已结算,3-部分结算|
|invoice_no|varchar|50|Y|NULL|发票号|
|settlement_id|bigint|-|Y|NULL|结算单ID，外键|
|remark|varchar|200|Y|NULL|备注|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
|update_time|datetime|-|N|CURRENT_TIMESTAMP ON UPDATE|更新时间|
##### 14. 结算单表 (t_settlement)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|settlement_no|varchar|32|N|-|结算单号，唯一索引|
|party_type|tinyint|-|N|-|结算方类型:1-客户,2-承运商|
|party_id|bigint|-|N|-|结算方ID（客户/承运商ID）|
|settlement_start|date|-|N|-|结算开始日期|
|settlement_end|date|-|N|-|结算结束日期|
|total_amount|decimal|(14,2)|N|-|总金额|
|paid_amount|decimal|(14,2)|Y|0|已付金额|
|unpaid_amount|decimal|(14,2)|Y|0|未付金额|
|status|tinyint|-|N|1|状态:1-待确认,2-已确认,3-部分付款,4-已结清,5-已取消|
|confirm_time|datetime|-|Y|NULL|确认时间|
|payment_time|datetime|-|Y|NULL|付款时间|
|invoice_status|tinyint|-|Y|0|开票状态:0-未开票,1-已开票,2-红冲|
|remark|varchar|200|Y|NULL|备注|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
|create_by|varchar|50|Y|NULL|创建人|
|update_time|datetime|-|N|CURRENT_TIMESTAMP ON UPDATE|更新时间|
##### 15. 回单表 (t_receipt)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|waybill_id|bigint|-|N|-|运单ID，外键|
|receipt_no|varchar|32|N|-|回单号，唯一索引|
|receipt_type|tinyint|-|N|1|类型:1-纸质回单,2-电子签收|
|signer_name|varchar|50|Y|NULL|签收人|
|signer_phone|varchar|20|Y|NULL|签收人电话|
|sign_time|datetime|-|Y|NULL|签收时间|
|sign_photo_url|varchar|500|Y|NULL|签收照片URL|
|receipt_file_url|varchar|500|Y|NULL|回单文件URL|
|status|tinyint|-|N|1|状态:1-未回传,2-已回传,3-已审核|
|return_time|datetime|-|Y|NULL|回传时间|
|audit_time|datetime|-|Y|NULL|审核时间|
|audit_by|varchar|50|Y|NULL|审核人|
|remark|varchar|200|Y|NULL|备注|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
|update_time|datetime|-|N|CURRENT_TIMESTAMP ON UPDATE|更新时间|
#### 4.3.6 系统日志表

##### 16. 操作日志表 (t_operation_log)

|字段名|类型|长度|允许空|默认值|说明|
|---|---|---|---|---|---|
|id|bigint|-|N|AUTO|主键ID|
|user_id|bigint|-|Y|NULL|操作用户ID|
|username|varchar|50|Y|NULL|用户名|
|operation_type|varchar|50|Y|NULL|操作类型（新增/修改/删除/查询）|
|operation_desc|varchar|500|Y|NULL|操作描述|
|request_url|varchar|200|Y|NULL|请求URL|
|request_params|text|-|Y|NULL|请求参数|
|response_result|text|-|Y|NULL|响应结果|
|ip_address|varchar|50|Y|NULL|IP地址|
|cost_time|int|-|Y|NULL|耗时(ms)|
|is_success|tinyint|-|Y|1|是否成功:1-是,0-否|
|error_msg|varchar|500|Y|NULL|错误信息|
|create_time|datetime|-|N|CURRENT_TIMESTAMP|创建时间|
### 4.4 索引设计

```SQL

-- 高频查询索引
CREATE INDEX idx_waybill_no ON t_waybill(waybill_no);
CREATE INDEX idx_waybill_customer_status ON t_waybill(customer_id, waybill_status);
CREATE INDEX idx_waybill_time ON t_waybill(expect_pickup_time, expect_delivery_time);
CREATE INDEX idx_dispatch_vehicle_status ON t_dispatch(vehicle_id, dispatch_status);
CREATE INDEX idx_dispatch_waybill ON t_dispatch(waybill_id);
CREATE INDEX idx_track_dispatch_time ON t_track_point(dispatch_id, location_time);
CREATE INDEX idx_event_dispatch_handled ON t_onway_event(dispatch_id, is_handled);
CREATE INDEX idx_cost_waybill_type ON t_cost_detail(waybill_id, cost_type);
CREATE INDEX idx_settlement_party_time ON t_settlement(party_type, party_id, settlement_start, settlement_end);
CREATE INDEX idx_receipt_waybill_status ON t_receipt(waybill_id, status);
CREATE INDEX idx_operation_user_time ON t_operation_log(user_id, create_time);
```

## 五、关键技术设计

### 5.1 缓存策略

|缓存Key|数据类型|过期时间|说明|
|---|---|---|---|
|customer:{id}|String|1h|客户信息|
|carrier:{id}|String|1h|承运商信息|
|vehicle:{id}|String|30min|车辆信息|
|driver:{id}|String|30min|司机信息|
|waybill:{no}|String|10min|运单信息|
|dispatch:route:{id}|String|5min|调度路径|
|warehouse:{id}|String|1h|仓库信息|
|hot:waybill:status:{status}|List|5min|热门状态运单列表|
### 5.2 消息队列Topic设计

|Topic|用途|消费者|
|---|---|---|
|waybill.created|运单创建|调度服务、报表服务|
|waybill.status.changed|运单状态变更|通知服务、报表服务|
|dispatch.started|调度发车|在途监控、短信通知|
|dispatch.completed|调度完成|结算服务、回单服务|
|gps.location|GPS定位|轨迹存储、事件检测|
|event.alert|异常报警|通知服务、客服系统|
|settlement.created|结算单创建|财务系统、发票服务|
|receipt.uploaded|回单上传|审核服务、结算服务|
### 5.3 API接口设计（RESTful）

|模块|端点|方法|说明|
|---|---|---|---|
|订单|/api/v1/waybills|POST|创建运单|
|订单|/api/v1/waybills/{id}|GET|查询运单详情|
|订单|/api/v1/waybills/{id}/status|PUT|更新运单状态|
|订单|/api/v1/waybills|GET|分页查询运单（多条件）|
|调度|/api/v1/dispatches/assign|POST|智能派车|
|调度|/api/v1/dispatches/{id}|GET|查询调度单详情|
|调度|/api/v1/dispatches/{id}/start|PUT|确认发车|
|在途|/api/v1/tracking/{dispatchId}|GET|查询实时轨迹|
|在途|/api/v1/events/{dispatchId}|GET|查询在途事件|
|结算|/api/v1/costs/calculate|POST|运费计算|
|结算|/api/v1/settlements|POST|创建结算单|
|结算|/api/v1/settlements/{id}/confirm|PUT|确认结算单|
|回单|/api/v1/receipts/upload|POST|上传回单|
|报表|/api/v1/reports/waybill-stat|GET|运单统计报表|
|报表|/api/v1/reports/cost-analysis|GET|成本分析报表|
### 5.4 性能优化

|瓶颈点|优化手段|
|---|---|
|订单查询|分页查询 + 索引 + 热点数据缓存|
|GPS轨迹写入|批量写入 + 异步处理 + 分表（按时间/车辆）|
|调度计算|异步调度 + 规则缓存 + 算法优化|
|报表统计|预计算 + 定时任务生成统计结果 + 缓存报表|
|高并发写入|消息队列削峰 + 数据库连接池优化 + 批量提交|
### 5.5 扩展性设计（微服务拆分预留）

|未来微服务模块|拆分依据|核心表|
|---|---|---|
|订单服务|订单全生命周期管理|t_waybill、t_waybill_detail|
|调度服务|运力调度与路径规划|t_dispatch、t_loading_order|
|跟踪服务|轨迹与在途事件管理|t_track_point、t_onway_event、t_temp_humidity|
|结算服务|费用与结算管理|t_cost_detail、t_settlement|
|基础数据服务|基础信息管理|t_customer、t_carrier、t_vehicle、t_driver、t_warehouse|
|报表服务|统计分析与报表生成|全表聚合查询|
|通知服务|消息与异常提醒|事件表、通知记录表|
## 六、总结

本TMS单体系统设计遵循“业务解耦 + 单体部署 + 模块清晰 + 可演进微服务”的核心思想，覆盖运输管理全流程，数据库设计兼顾业务完整性与扩展性，技术方案聚焦高可用、高性能与易维护性。系统可先跑通核心业务，再根据实际业务增长逐步优化性能、拆分微服务，适配中大型企业不同阶段的运输管理需求。
> （注：文档部分内容可能由 AI 生成）