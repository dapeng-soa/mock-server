namespace java com.today.api.order.service

include 'order_request_new.thrift'
include 'order_response_new.thrift'
include 'order_vo_new.thrift'
include 'order_query_service_request.thrift'

service OrderQueryService {

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
    order_response_new.TQueryOrderListResponseNew queryOrderList(order_request_new.TQueryOrderListRequestNew request)

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
     list<order_vo_new.TOrderPaymentNew> getOrderPaymentsByOrderNo(1: string orderNo)


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
        order_response_new.TQueryOrderPaymentSumListResponseNew queryOrderPaymentSum(order_request_new.TQueryOrderPaymentSumListRequestNew request)

    /**
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
    list<order_vo_new.TOrderDetailNew> getOrderDetailByIds(1: list<i64> orderDetailIds)

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
    order_response_new.TGetOrderSummaryResponseNew getOrderSummaryByOrderId(1: i64 orderId)

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
    order_response_new.TGetOrderSummaryResponseNew getOrderSummaryByOrderNo(1: string orderNo)

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
    list<order_vo_new.TOrderDetailNew> queryDoneOrderDetailByStore(1/*request*/: order_request_new.ListDoneOrderDetailRequestNew request)

    /**
    #  根据订单号，支付序号查找对应的支付流水
    ## 业务描述
       根据订单号，支付序号查找对应的支付流水
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
       支付流水
    */
    order_vo_new.TOrderPaymentNew queryOrderPayment(order_query_service_request.TQueryOrderPaymentRequest request)

    /**
    *  根据订单号查询订单基础信息
    **/
    order_vo_new.TOrderNew getOrderByOrderNo(1: string orderNo)
    /**
    # 根据 查询条件 查询线上订单列表
    ## 业务描述
        根据 查询条件 查询订单包括mini货架、预定自提、扫码购物、样品等...
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
       返回线上订单列表页面
    */
     order_response_new.TQueryOrderListResponseNew queryOnlineOrderList(1: order_request_new.TQueryOrderListRequestNew request)
}(group="Order")