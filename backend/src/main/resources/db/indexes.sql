-- TMS数据库索引优化脚本
-- 创建时间: 2026-05-07
-- 说明: 为提高查询性能，为常用查询字段创建索引

-- ============================================
-- 运单相关表索引
-- ============================================

-- 运单表索引
CREATE INDEX IF NOT EXISTS idx_tms_waybill_waybill_no ON tms_waybill(waybill_no);
CREATE INDEX IF NOT EXISTS idx_tms_waybill_order_no ON tms_waybill(order_no);
CREATE INDEX IF NOT EXISTS idx_tms_waybill_customer_id ON tms_waybill(customer_id);
CREATE INDEX IF NOT EXISTS idx_tms_waybill_waybill_status ON tms_waybill(waybill_status);
CREATE INDEX IF NOT EXISTS idx_tms_waybill_create_time ON tms_waybill(create_time);
CREATE INDEX IF NOT EXISTS idx_tms_waybill_expect_delivery_time ON tms_waybill(expect_delivery_time);

-- 运单明细表索引
CREATE INDEX IF NOT EXISTS idx_tms_waybill_detail_waybill_id ON tms_waybill_detail(waybill_id);
CREATE INDEX IF NOT EXISTS idx_tms_waybill_detail_sku_code ON tms_waybill_detail(sku_code);

-- ============================================
-- 调度相关表索引
-- ============================================

-- 调度单表索引
CREATE INDEX IF NOT EXISTS idx_tms_dispatch_dispatch_no ON tms_dispatch(dispatch_no);
CREATE INDEX IF NOT EXISTS idx_tms_dispatch_waybill_id ON tms_dispatch(waybill_id);
CREATE INDEX IF NOT EXISTS idx_tms_dispatch_vehicle_id ON tms_dispatch(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_tms_dispatch_driver_id ON tms_dispatch(driver_id);
CREATE INDEX IF NOT EXISTS idx_tms_dispatch_carrier_id ON tms_dispatch(carrier_id);
CREATE INDEX IF NOT EXISTS idx_tms_dispatch_dispatch_status ON tms_dispatch(dispatch_status);
CREATE INDEX IF NOT EXISTS idx_tms_dispatch_planned_start_time ON tms_dispatch(planned_start_time);

-- 装货单表索引
CREATE INDEX IF NOT EXISTS idx_tms_loading_order_loading_no ON tms_loading_order(loading_no);
CREATE INDEX IF NOT EXISTS idx_tms_loading_order_dispatch_id ON tms_loading_order(dispatch_id);
CREATE INDEX IF NOT EXISTS idx_tms_loading_order_warehouse_id ON tms_loading_order(warehouse_id);
CREATE INDEX IF NOT EXISTS idx_tms_loading_order_status ON tms_loading_order(status);

-- ============================================
-- 在途监控相关表索引
-- ============================================

-- 轨迹点表索引
CREATE INDEX IF NOT EXISTS idx_tms_track_point_dispatch_id ON tms_track_point(dispatch_id);
CREATE INDEX IF NOT EXISTS idx_tms_track_point_vehicle_id ON tms_track_point(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_tms_track_point_location_time ON tms_track_point(location_time);
CREATE INDEX IF NOT EXISTS idx_tms_track_point_dispatch_time ON tms_track_point(dispatch_id, location_time);

-- 在途事件表索引
CREATE INDEX IF NOT EXISTS idx_tms_onway_event_dispatch_id ON tms_onway_event(dispatch_id);
CREATE INDEX IF NOT EXISTS idx_tms_onway_event_waybill_id ON tms_onway_event(waybill_id);
CREATE INDEX IF NOT EXISTS idx_tms_onway_event_event_type ON tms_onway_event(event_type);
CREATE INDEX IF NOT EXISTS idx_tms_onway_event_event_level ON tms_onway_event(event_level);
CREATE INDEX IF NOT EXISTS idx_tms_onway_event_is_handled ON tms_onway_event(is_handled);
CREATE INDEX IF NOT EXISTS idx_tms_onway_event_event_time ON tms_onway_event(event_time);

-- 温湿度记录表索引
CREATE INDEX IF NOT EXISTS idx_tms_temp_humidity_dispatch_id ON tms_temp_humidity(dispatch_id);
CREATE INDEX IF NOT EXISTS idx_tms_temp_humidity_record_time ON tms_temp_humidity(record_time);

-- ============================================
-- 财务结算相关表索引
-- ============================================

-- 结算单表索引
CREATE INDEX IF NOT EXISTS idx_tms_settlement_settlement_no ON tms_settlement(settlement_no);
CREATE INDEX IF NOT EXISTS idx_tms_settlement_party_type ON tms_settlement(party_type);
CREATE INDEX IF NOT EXISTS idx_tms_settlement_party_id ON tms_settlement(party_id);
CREATE INDEX IF NOT EXISTS idx_tms_settlement_status ON tms_settlement(status);
CREATE INDEX IF NOT EXISTS idx_tms_settlement_settlement_start ON tms_settlement(settlement_start);
CREATE INDEX IF NOT EXISTS idx_tms_settlement_settlement_end ON tms_settlement(settlement_end);

-- 费用明细表索引
CREATE INDEX IF NOT EXISTS idx_tms_cost_detail_waybill_id ON tms_cost_detail(waybill_id);
CREATE INDEX IF NOT EXISTS idx_tms_cost_detail_cost_type ON tms_cost_detail(cost_type);
CREATE INDEX IF NOT EXISTS idx_tms_cost_detail_carrier_id ON tms_cost_detail(carrier_id);

-- 回单表索引
CREATE INDEX IF NOT EXISTS idx_tms_receipt_receipt_no ON tms_receipt(receipt_no);
CREATE INDEX IF NOT EXISTS idx_tms_receipt_waybill_id ON tms_receipt(waybill_id);
CREATE INDEX IF NOT EXISTS idx_tms_receipt_status ON tms_receipt(status);

-- ============================================
-- 基础数据相关表索引
-- ============================================

-- 客户表索引
CREATE INDEX IF NOT EXISTS idx_tms_customer_customer_code ON tms_customer(customer_code);
CREATE INDEX IF NOT EXISTS idx_tms_customer_customer_name ON tms_customer(customer_name);
CREATE INDEX IF NOT EXISTS idx_tms_customer_status ON tms_customer(status);

-- 承运商表索引
CREATE INDEX IF NOT EXISTS idx_tms_carrier_carrier_code ON tms_carrier(carrier_code);
CREATE INDEX IF NOT EXISTS idx_tms_carrier_carrier_name ON tms_carrier(carrier_name);
CREATE INDEX IF NOT EXISTS idx_tms_carrier_status ON tms_carrier(status);

-- 车辆表索引
CREATE INDEX IF NOT EXISTS idx_tms_vehicle_plate_number ON tms_vehicle(plate_number);
CREATE INDEX IF NOT EXISTS idx_tms_vehicle_carrier_id ON tms_vehicle(carrier_id);
CREATE INDEX IF NOT EXISTS idx_tms_vehicle_status ON tms_vehicle(status);

-- 司机表索引
CREATE INDEX IF NOT EXISTS idx_tms_driver_id_card ON tms_driver(id_card);
CREATE INDEX IF NOT EXISTS idx_tms_driver_phone ON tms_driver(phone);
CREATE INDEX IF NOT EXISTS idx_tms_driver_status ON tms_driver(status);

-- 仓库表索引
CREATE INDEX IF NOT EXISTS idx_tms_warehouse_warehouse_code ON tms_warehouse(warehouse_code);
CREATE INDEX IF NOT EXISTS idx_tms_warehouse_warehouse_name ON tms_warehouse(warehouse_name);
CREATE INDEX IF NOT EXISTS idx_tms_warehouse_status ON tms_warehouse(status);

-- ============================================
-- 系统管理相关表索引
-- ============================================

-- 用户表索引
CREATE INDEX IF NOT EXISTS idx_tms_user_username ON tms_user(username);
CREATE INDEX IF NOT EXISTS idx_tms_user_status ON tms_user(status);

-- 角色表索引
CREATE INDEX IF NOT EXISTS idx_tms_role_role_code ON tms_role(role_code);
CREATE INDEX IF NOT EXISTS idx_tms_role_status ON tms_role(status);

-- 权限表索引
CREATE INDEX IF NOT EXISTS idx_tms_permission_permission_code ON tms_permission(permission_code);
CREATE INDEX IF NOT EXISTS idx_tms_permission_parent_id ON tms_permission(parent_id);
CREATE INDEX IF NOT EXISTS idx_tms_permission_permission_type ON tms_permission(permission_type);

-- 用户角色关联表索引
CREATE INDEX IF NOT EXISTS idx_tms_user_role_user_id ON tms_user_role(user_id);
CREATE INDEX IF NOT EXISTS idx_tms_user_role_role_id ON tms_user_role(role_id);

-- 角色权限关联表索引
CREATE INDEX IF NOT EXISTS idx_tms_role_permission_role_id ON tms_role_permission(role_id);
CREATE INDEX IF NOT EXISTS idx_tms_role_permission_permission_id ON tms_role_permission(permission_id);

-- 操作日志表索引
CREATE INDEX IF NOT EXISTS idx_tms_operation_log_username ON tms_operation_log(username);
CREATE INDEX IF NOT EXISTS idx_tms_operation_log_operation ON tms_operation_log(operation);
CREATE INDEX IF NOT EXISTS idx_tms_operation_log_create_time ON tms_operation_log(create_time);

-- ============================================
-- 复合索引（针对常用组合查询）
-- ============================================

-- 运单状态+创建时间复合索引（用于运单列表查询）
CREATE INDEX IF NOT EXISTS idx_tms_waybill_status_create_time ON tms_waybill(waybill_status, create_time);

-- 调度单状态+计划发车时间复合索引
CREATE INDEX IF NOT EXISTS idx_tms_dispatch_status_start_time ON tms_dispatch(dispatch_status, planned_start_time);

-- 在途事件处理状态+事件级别复合索引
CREATE INDEX IF NOT EXISTS idx_tms_onway_event_handled_level ON tms_onway_event(is_handled, event_level);

-- 结算单结算方+状态复合索引
CREATE INDEX IF NOT EXISTS idx_tms_settlement_party_status ON tms_settlement(party_type, party_id, status);
