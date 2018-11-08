namespace java com.today.api.dailyorder.service

include 'dailyorder_request.thrift'
include 'dailyorder_response.thrift'

/**
* 日结服务
**/
service DailyOrderService {
/**
# 日结每日营业额接口
## 业务描述
   需要查询门店每天pos机根据口座分类的日结营业额
## 接口依赖
    ListSkuDetailByNosQuery
## 边界异常说明
    无
## 输入
    storeId 门店ID
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   门店每天pos机根据口座分类的日结营业额,以及汇总总览（总代收，总代售，礼品卡充值，礼品卡充值促销），支付总览（移动支付，刷卡支付，会员支付，外卖，城市一卡通），现金总览（备用金，银行送金金额），其他总览（报废金额）
*/
dailyorder_response.TQueryOrderDailySumListResponse orderDailySumQuery(dailyorder_request.TQueryOrderDailySumListRequest request)

/**
# 日结支付详情列表接口
## 业务描述
   需要查询门店每天不同支付方式的支付详情
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    storeId 门店ID
    tradeType 支付方式
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   门店每天不同支付方式的支付详情(支付时间，订单号，支付金额，pos机号，收银员，工号,支付方式)
*/
dailyorder_response.TQueryPaymentDetailListResponse paymentDetailQuery(dailyorder_request.TQueryPaymentDetailListRequest request)

/**
# 日结促销详情列表接口
## 业务描述
   需要查询门店每天不同促销详情
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    storeId 门店ID
    businessTime 营业时间
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   门店每天不同的促销详情(优惠劵id,优惠劵类型,订单号,订单总金额,订单实际付款金额,优惠金额,订单完成时间,POS机编号,工号)
*/
dailyorder_response.TQueryPromotionDetailListResponse promotionDetailQuery(dailyorder_request.TQueryPromotionDetailListRequest request)

/**
# 日结报废详情列表接口
## 业务描述
   需要查询门店每天报废详情
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    storeId 门店ID
    businessTime 营业时间
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   门店每天的报废详情
*/
dailyorder_response.ScrapDetailListResponse scrapDetailListQuery(dailyorder_request.TQueryScrapDetailListRequest request)

/**
# 日结列表接口
## 业务描述
   需要查询门店日结列表
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    storeId 门店ID
    page 分页
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   门店日结列表
*/
dailyorder_response.ListDailyReportResponse dailyReportListQuery(dailyorder_request.ListDailyReportListRequest request)

/**
# 手动落地日结数据接口
## 业务描述
   手动落地日结数据
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    storeId 门店ID
    businessTime 营业时间
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   无
*/
void SaveDailyOrderReport(dailyorder_request.TQueryOrderDailySumListRequest request)

/**
# Z账接口
## 业务描述
   需要查询门店Z账
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    storeId 门店ID
    businessTime营业时间
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   Z账详情
*/
dailyorder_response.ListZOrderResponse zOrderQuery(dailyorder_request.TQueryZOrderRequest request)
}(group="Order")