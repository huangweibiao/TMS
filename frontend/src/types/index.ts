/**
 * 通用类型定义
 */

// 分页响应
export interface PageResult<T> {
  pageNum: number
  pageSize: number
  total: number
  pages: number
  list: T[]
}

// 通用响应
export interface Result<T> {
  code: number
  message: string
  data: T
  timestamp: number
}

// 登录请求
export interface LoginRequest {
  username: string
  password: string
}

// 登录响应
export interface LoginResponse {
  token: string
  tokenType: string
  userId: number
  username: string
  realName: string
  roles: string[]
}

// 用户信息
export interface UserInfo {
  id: number
  username: string
  realName: string
  phone: string
  email: string
  roles: string[]
}

// 客户
export interface Customer {
  id: number
  customerCode: string
  customerName: string
  shortName: string
  contactPerson: string
  contactPhone: string
  contactAddress: string
  creditLevel: number
  settlementCycle: number
  status: number
  createTime: string
  updateTime: string
}

// 承运商
export interface Carrier {
  id: number
  carrierCode: string
  carrierName: string
  carrierType: number
  contactPerson: string
  contactPhone: string
  status: number
}

// 车辆
export interface Vehicle {
  id: number
  plateNumber: string
  vehicleType: string
  vehicleBrand: string
  loadCapacity: number
  volumeCapacity: number
  ownerType: number
  status: number
}

// 司机
export interface Driver {
  id: number
  driverName: string
  idCard: string
  phone: string
  licenseType: string
  status: number
}

// 仓库
export interface Warehouse {
  id: number
  warehouseCode: string
  warehouseName: string
  warehouseType: number
  province: string
  city: string
  district: string
  address: string
  status: number
}

// 运单明细
export interface WaybillDetail {
  id: number
  waybillId: number
  skuCode?: string
  skuName?: string
  quantity: number
  unit: string
  unitWeight?: number
  unitVolume?: number
  unitPrice?: number
  totalPrice?: number
  remark?: string
}

// 运单
export interface Waybill {
  id: number
  waybillNo: string
  orderNo?: string
  customerId: number
  customerName?: string
  carrierId?: number
  productType?: string
  totalWeight?: number
  totalVolume?: number
  totalQuantity?: number
  goodsValue?: number
  isHazardous?: number
  isFragile?: number
  needTemperature?: number
  minTemp?: number
  maxTemp?: number
  shipperName: string
  shipperPhone: string
  shipperAddress: string
  consigneeName: string
  consigneePhone: string
  consigneeAddress: string
  expectPickupTime?: string
  expectDeliveryTime?: string
  actualPickupTime?: string
  actualDeliveryTime?: string
  waybillStatus: number
  remark?: string
  createBy?: string
  createTime?: string
  updateTime?: string
  details?: WaybillDetail[]
}

// 调度单
export interface Dispatch {
  id: number
  dispatchNo: string
  waybillId: number
  waybillNo?: string
  vehicleId: number
  plateNumber?: string
  driverId: number
  driverName?: string
  carrierId?: number
  routePlan?: string
  plannedDistance?: number
  actualDistance?: number
  plannedStartTime?: string
  plannedEndTime?: string
  actualStartTime?: string
  actualEndTime?: string
  dispatchStatus: number
  createBy?: string
  createTime?: string
  updateTime?: string
}

// 装货单
export interface LoadingOrder {
  id: number
  loadingNo: string
  dispatchId: number
  dispatchNo?: string
  warehouseId: number
  warehouseName?: string
  loadingTime?: string
  operator?: string
  status: number
  remark?: string
  createTime?: string
  updateTime?: string
}

// 结算单
export interface Settlement {
  id: number
  settlementNo: string
  partyType: number
  partyId: number
  partyName?: string
  settlementStart: string
  settlementEnd: string
  totalAmount: number
  paidAmount: number
  unpaidAmount: number
  status: number
  confirmTime?: string
  paymentTime?: string
  invoiceStatus: number
  remark?: string
  createBy?: string
  createTime?: string
  updateTime?: string
}

// 回单
export interface Receipt {
  id: number
  waybillId: number
  waybillNo?: string
  receiptNo: string
  receiptType: number
  signerName?: string
  signerPhone?: string
  signTime?: string
  signPhotoUrl?: string
  receiptFileUrl?: string
  status: number
  returnTime?: string
  auditTime?: string
  auditBy?: string
  remark?: string
  createTime?: string
  updateTime?: string
}

// 在途事件
export interface OnwayEvent {
  id: number
  dispatchId: number
  dispatchNo?: string
  waybillId: number
  waybillNo?: string
  eventType: number
  eventLevel: number
  eventContent?: string
  location?: string
  eventTime: string
  isHandled: number
  handleResult?: string
  handleTime?: string
  handleBy?: string
  createTime?: string
  updateTime?: string
}

// 轨迹点
export interface TrackPoint {
  id: number
  dispatchId: number
  vehicleId: number
  plateNumber?: string
  longitude: number
  latitude: number
  speed?: number
  direction?: number
  locationTime: string
  createTime?: string
  updateTime?: string
}

// 权限
export interface Permission {
  id: number
  permissionCode: string
  permissionName: string
  permissionType: number
  parentId?: number
  parentName?: string
  path?: string
  sortOrder?: number
  status: number
  createTime?: string
  updateTime?: string
}

// 操作日志
export interface OperationLog {
  id: number
  username: string
  operation: string
  method: string
  params?: string
  result?: string
  ip?: string
  duration: number
  createTime?: string
}

// 报表数据
export interface ReportData {
  date?: string
  waybillCount?: number
  totalWeight?: number
  totalVolume?: number
  totalAmount?: number
  onTimeRate?: number
  damageRate?: number
}

// 温湿度记录
export interface TempHumidity {
  id: number
  dispatchId: number
  dispatchNo?: string
  deviceId: string
  temperature: number
  humidity: number
  recordTime: string
  createTime?: string
  updateTime?: string
}

// 用户
export interface User {
  id: number
  username: string
  realName?: string
  phone?: string
  email?: string
  status: number
  lastLoginTime?: string
  lastLoginIp?: string
  roleIds?: number[]
  roleNames?: string[]
  createTime?: string
  updateTime?: string
}

// 角色
export interface Role {
  id: number
  roleCode: string
  roleName: string
  description?: string
  status: number
  permissionIds?: number[]
  permissionNames?: string[]
  createTime?: string
  updateTime?: string
}

// 客户（补充完整字段）
export interface CustomerFull {
  id: number
  customerCode: string
  customerName: string
  shortName?: string
  customerType?: number
  contactPerson?: string
  contactPhone?: string
  contactAddress?: string
  province?: string
  city?: string
  district?: string
  longitude?: number
  latitude?: number
  email?: string
  fax?: string
  bankName?: string
  bankAccount?: string
  taxNo?: string
  licenseNo?: string
  creditLevel: number
  settlementCycle: number
  status: number
  createTime?: string
  updateTime?: string
}

// 承运商（补充完整字段）
export interface CarrierFull {
  id: number
  carrierCode: string
  carrierName: string
  carrierType?: number
  licenseNo?: string
  taxNo?: string
  contactPerson?: string
  contactPhone?: string
  cooperationStart?: string
  cooperationEnd?: string
  rating?: number
  status: number
  createTime?: string
  updateTime?: string
}

// 车辆（补充完整字段）
export interface VehicleFull {
  id: number
  plateNumber: string
  vehicleType?: string
  vehicleBrand?: string
  loadCapacity?: number
  volumeCapacity?: number
  length?: number
  width?: number
  height?: number
  ownerType?: number
  carrierId?: number
  gpsDeviceId?: string
  status: number
  lastMaintenance?: string
  createTime?: string
  updateTime?: string
}

// 司机（补充完整字段）
export interface DriverFull {
  id: number
  driverName: string
  idCard: string
  phone: string
  licenseType?: string
  licenseNo?: string
  hireDate?: string
  carrierId?: number
  vehicleId?: number
  status: number
  createTime?: string
  updateTime?: string
}

// 仓库（补充完整字段）
export interface WarehouseFull {
  id: number
  warehouseCode: string
  warehouseName: string
  warehouseType?: number
  province?: string
  city?: string
  district?: string
  address?: string
  longitude?: number
  latitude?: number
  contactPerson?: string
  contactPhone?: string
  status: number
  createTime?: string
  updateTime?: string
}

// 地址库
export interface Address {
  id: number
  addressCode?: string
  addressName?: string
  addressType?: number
  customerId?: number
  contactName?: string
  contactPhone?: string
  province?: string
  city?: string
  district?: string
  detailAddress?: string
  fullAddress?: string
  longitude?: number
  latitude?: number
  zipCode?: string
  isDefault?: number
  status: number
  remark?: string
  createTime?: string
  updateTime?: string
}

// 产品类型
export interface ProductType {
  id: number
  typeCode: string
  typeName: string
  parentId?: number
  level?: number
  needTemperature?: number
  isHazardous?: number
  isFragile?: number
  defaultUnitWeight?: number
  defaultUnitVolume?: number
  sortOrder?: number
  status: number
  remark?: string
  createTime?: string
  updateTime?: string
}

// 系统配置
export interface SystemConfig {
  id: number
  configKey: string
  configValue: string
  configName?: string
  description?: string
  configType?: number
  isEditable?: number
  configGroup?: string
  sortOrder?: number
  status: number
  createTime?: string
  updateTime?: string
}

// 费用明细
export interface CostDetail {
  id: number
  waybillId: number
  waybillNo?: string
  costType: number
  amount: number
  currency?: string
  direction: number
  settlementStatus?: number
  invoiceNo?: string
  settlementId?: number
  remark?: string
  createTime?: string
  updateTime?: string
}

// 发票
export interface Invoice {
  id: number
  invoiceNo: string
  invoiceType: number
  settlementId?: number
  settlementNo?: string
  partyType: number
  partyId: number
  partyName?: string
  amount: number
  taxAmount?: number
  totalAmount: number
  invoiceDate: string
  status: number
  remark?: string
  createBy?: string
  createTime?: string
  updateTime?: string
}

// 对账单
export interface Reconciliation {
  id: number
  reconNo: string
  partyType: number
  partyId: number
  partyName?: string
  reconStart: string
  reconEnd: string
  totalAmount: number
  confirmedAmount?: number
  diffAmount?: number
  status: number
  confirmTime?: string
  confirmBy?: string
  remark?: string
  createBy?: string
  createTime?: string
  updateTime?: string
}

// 阶梯价格配置
export interface PriceConfig {
  id: number
  configName: string
  configType: number
  customerId?: number
  carrierId?: number
  startWeight?: number
  endWeight?: number
  startVolume?: number
  endVolume?: number
  startDistance?: number
  endDistance?: number
  unitPrice: number
  minPrice?: number
  maxPrice?: number
  effectiveDate: string
  expiryDate?: string
  status: number
  remark?: string
  createTime?: string
  updateTime?: string
}
