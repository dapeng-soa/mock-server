namespace java com.today.api.order.service

include 'order_vo.thrift'
include 'order_request.thrift'
include 'order_response.thrift'
include 'order_event.thrift'

service OrderService {


/**
# 批量生成订单
## 业务描述
    批量生成订单
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    订单入库，子单入库，优惠券状态更新，支付流水入库
##  事务处理
    无
## 前置检查
   判断订单是否存在, 优惠券是否已使用
## 逻辑处理
    
    1. 插入订单， 插入子单
    2. 检查优惠券状态， 如果优惠券是已使用，订单创建失败，否则锁定优惠券
    3. 如果优惠券是现金券， 记一笔支付流水，
    4. 如果订单是现金支付，此时包含支付信息， 则支付信息入库
    5. 检查订单是否已经完成支付，完成支付，置为已完成

## 输出
   返回成功和失败的订单记录

*/
    void createOrderList(list<order_request.TOrderRequest> request)(events="com.today.api.order.events.CreateOrderEvent")

/**
# 取消订单
## 业务描述
    取消订单
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    把订单信息插入到数据库
##  事务处理
    无
## 前置检查
   判断订单是否存在
## 逻辑处理

    1. 断定订单是否已取消，如果已取消，则直接返回已取消，否则将订单状态制为已取消
    2. 对已支付的流水进行退款，如果成功，则状态改为已退款。如果出现异常，对该流水状态改为待异常处理，跳过继续退款其他流水
    3. 对已使用的优惠券进行还原
    4. 对前台返回成功
    5. 如果退款失败的流水，进行跑P处理

## 输出
   无

*/
    void cancelOrder(order_request.TOrderCancelRequest request)(events="com.today.api.order.events.CancelOrderEvent")

/**
# 整单退款接口
## 业务描述
    整单退款，但是不修改订单状态
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    把订单信息插入到数据库
##  事务处理
    无
## 前置检查
   判断订单是否存在
## 逻辑处理

## 输出
   无

*/
    void cancelOrderRefund(order_request.TOrderCancelRequest request)


/**
# 支付
## 业务描述
    支付，调用非码接口完成各种在线渠道的支付
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    无
##  事务处理
    无
## 前置检查
   判断订单是否存在
## 逻辑处理

    1. 支付的时候pos向我传一个trans_id，保证单次支付的幂等性
    2. 首先在本地保存一条未支付的支付流水
    3. 调用非码，发起支付
    4. 支付成功或者失败，修改支付流水状态
    5. 如果超时，安全时间后，则发起冲正接口，对该条记录进行冲正，如果成功，该条记录置为失败(如果此时发起订单取消，则返回取消成功，安全时间后发起冲正取消该笔支付)
    6. 判断订单实收等于应收， 如果是，状态改为已完成，同时修改优惠券状态为已使用

## 输出
    第三方支付流水等信息
*/
    order_response.TOrderPaymentResponse createOrderPayment(order_request.TOrderPaymentRequest request)
    (events="com.today.api.order.events.StoreOrderEndEvent")

/**
# 取消支付(不取消商品)
## 业务描述
    取消支付，调用非码接口退款
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    无
##  事务处理
    无
## 前置检查
   判断订单是否存在，如果是单一支付的订单，才可发起退款
## 逻辑处理

    1. 判断该条流水是否存在，如果存在，新增一条退款流水，状态为退款中
    2. 调用非码接口退款
    3. 如果成功，修改退款流水状态
    4. 如果异常，标记状态，依然返回前端退款成功
    5. 定时任务进行重试异常的流水


## 输出
    无
*/
    void cancelOrderPayment(order_request.TOrderPaymentCancelRequest request)
/**
# 取消商品
## 业务描述
    取消商品，调用非码接口退款
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    无
##  事务处理
    无
## 前置检查
   判断订单是否存在
## 逻辑处理

    1. 判断该条流水是否存在，该条sku在订单里是否存在，如果存在，新增一条退款流水，状态为退款中
    2. 调用非码接口退款
    3. 如果成功，修改退款流水状态
    4. 如果异常，标记状态，依然返回前端退款成功
    5. 修改订单里该条商品的状态
    6. 定时任务进行重试异常的流水

## 输出
    无
*/
    void cancelOrderSku(order_request.TOrderSkuRequest request)(events="com.today.api.order.events.PartRefundEvent")
/**
# 根据 查询条件 查询订单列表
## 业务描述
    根据 查询条件 查询订单列表
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   返回订单列表
*/
    order_response.TQueryOrderListResponse queryOrderList(order_request.TQueryOrderListRequest request)

/**
# 根据 查询条件 查询订单支付列表
## 业务描述
    根据 查询条件 查询订单支付列表
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   返回订单支付列表
*/
    order_response.TOrderPaymentListResponse queryOrderPaymentList(order_request.TPaymentRequest request)


/**
# 查询营收总金额
## 业务描述
    根据店铺id、pos机id统计当天的营收总金额
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   返回营收总金额
*/
    order_response.TQueryOrderPaymentSumListResponse queryOrderPaymentSum(order_request.TQueryOrderPaymentSumListRequest request)


/**
# 给后台用的订单详情接口
## 业务描述
   需要查询订单信息，商品信息，优惠劵信息，支付信息
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   订单列表，商品列表，优惠劵列表，支付列表
*/
order_response.TQueryOrderDetailResponse queryOrderDetail(order_request.TOrderPaymentListRequest request)/**
# 给后台用的订单详情接口
## 业务描述
   需要查询订单详情Ids查询 订单详情信息
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   订单详情信息列表
*/
 order_response.ListOrderDetailByIdsResponse listOrderDetailByIds(order_request.ListOrderDetailByIdsRequest request)

/**
# 通过订单ID查询订单详情
## 业务描述
   需要查询订单信息，商品信息，优惠劵信息，支付信息
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   订单列表，商品列表，优惠劵列表，支付列表
*/
order_response.TQueryOrderDetailResponse queryOrderDetailByOrderId(order_request.TQueryOrderDeatilRequest request)

/**
# 通过门店编号查询已完成商品订单列表
## 业务描述
   通过门店编号查询已完成商品订单列表(盘点用)
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理

## 输出
   订单商品列表
*/
list<order_vo.TOrderDetail> listOrderDetailByStore(1/*request*/: order_request.ListDoneOrderDetailRequest request)

/**
# 创建离线订单接口
## 业务描述
    批量生成订单
## 接口依赖
    无
## 边界异常说明
    无
## 输入
    无
## 数据库变更
    无
##  事务处理
    无
## 前置检查
    无
## 逻辑处理
    支付方式只有现金
## 输出

*/
void createOfflineOrderList(list<order_request.TOfflineOrderRequest> request)
}(group="Order")


