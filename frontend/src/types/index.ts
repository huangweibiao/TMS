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

// 运单
export interface Waybill {
  id: number
  waybillNo: string
  orderNo: string
  customerId: number
  customerName?: string
  productType: string
  totalWeight: number
  totalVolume: number
  totalQuantity: number
  shipperName: string
  shipperPhone: string
  shipperAddress: string
  consigneeName: string
  consigneePhone: string
  consigneeAddress: string
  expectPickupTime: string
  expectDeliveryTime: string
  waybillStatus: number
  remark: string
  createTime: string
}
