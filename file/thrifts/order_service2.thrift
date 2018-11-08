namespace java com.today.api.order.service

include 'order_request_new.thrift'
include 'order_response_new.thrift'
include 'order_event_new.thrift'
include 'order_vo_new.thrift'

service OrderService2 {


/**
# 1. 绑定终端（针对  翼码支付）
## 业务描述
    pos机第一次发起请求，需要绑定终端
## 接口依赖
    无
*/
    void bindPosTerminal(1: order_request_new.TBindPosRequest request)


    /**
    # 1. 生成在线订单
    ## 业务描述
        生成在线订单
    ## 接口依赖
        无
    ## 边界异常说明
        无
    ## 输入
        TOrderRequest request
    ## 数据库变更
        1. 主单入库
        2. 子单入库
    ##  事务处理
        无
    ## 前置检查
       1. 判断订单是否存在, 如果订单已存在，提示异常: 订单已存在
       2. order.discount_amount = sum(subOrder.discount_amount) + sum(coupon.discount_price)  (coupon.type = 商品券)
       3. order.actual_amount = sum(subOrder.sku_amount - subOrder.discount_amount) - sum(coupon.discount_price) (coupon.type = 商品券)
       4. order.orderTotalAmount = order.orderActualAmount + order.discountAmount

    ## 逻辑处理

        1. 插入订单
        2. 插入子单
        3. 发送CreateOrderEvent 事件通知消费者
    ## 输出
        无
    */
    void createOrder(1: order_request_new.TCreateOrderRequestNew request)
    (events="com.today.api.order.events.CreateOrderEventNew")

    /**
        # 1. 生成在线订单
        ## 业务描述
            生成在线订单
        ## 接口依赖
            无
        ## 边界异常说明
            无
        ## 输入
            TOrderRequest request
        ## 数据库变更
            1. 主单入库
            2. 子单入库
        ##  事务处理
            无
        ## 前置检查
           1. 判断订单是否存在, 如果订单已存在，提示异常: 订单已存在
           2. order.discount_amount = sum(subOrder.discount_amount) + sum(coupon.discount_price)  (coupon.type = 商品券)
           3. order.actual_amount = sum(subOrder.sku_amount - subOrder.discount_amount) - sum(coupon.discount_price) (coupon.type = 商品券)
           4. order.orderTotalAmount = order.orderActualAmount + order.discountAmount

        ## 逻辑处理

            1. 插入订单
            2. 插入子单
            3. 发送CreateOrderEvent 事件通知消费者
        ## 输出
            成功的订单号列表
        */
    list<string> createOrders(1: list<order_request_new.TCreateOrderRequestNew> request)
    (events="com.today.api.order.events.CreateOrderEventNew")

    /**
    * 创建离线订单
    **/
    void createOfflineOrder(1: order_request_new.TCreateOrderRequestNew request)
    (events="com.today.api.order.events.CreateOrderEventNew")

    /**
    * 批量创建离线订单
    **/
    void createOfflineOrders(1: list<order_request_new.TCreateOrderRequestNew> request)
    (events="com.today.api.order.events.CreateOrderEventNew")

    /**
    * 创建一笔订单支付(如果有多次支付，需多次调用)
    **/
    order_response_new.TOrderPaymentResponseNew createOrderPayment(order_request_new.TCreateOrderPaymentRequestNew request)
    (events="com.today.api.order.events.StoreOrderEndEventNew")

    /**
    * 创建多笔订单支付
    **/
    list<order_response_new.TOrderPaymentResponseNew> createOrderPayments(1: list<order_request_new.TCreateOrderPaymentRequestNew> request)
    (events="com.today.api.order.events.StoreOrderEndEventNew")

    /**
    * 取消订单的sku,新增返回结果
    **/
    order_response_new.TCancelOrderSkuResponseNew cancelOrderSku(1: order_request_new.TCancelOrderSkuRequestNew request)
    (events="com.today.api.order.events.PartRefundEventNew")

    /**
    * 根据 orderNo 批量取消订单的sku
    **/
    list<i32> cancelOrderSkus(1: string orderNo)
    (events="com.today.api.order.events.PartRefundEventNew")

    /**
    * 取消订单支付
    **/
    void cancelOrderPayment(order_request_new.TCancelOrderPaymentRequest request)

    /**
    *
    **/
    void cancelOrder(order_request_new.TCancelOrderRequest request)
    (events="com.today.api.order.events.CancelOrderEventNew")

    /**
    *  取消订单所有支付，但是不取消订单
    **/
    void cancelWholeOrderPayments(1: order_request_new.CancelWholeOrderPaymentsRequest request)

    /**
    * 包装事件所需要的元数据信息
    **/
    order_response_new.TOrderAndOrderDetailResponse wrapMetaDataForEvent()(virtual="true")

    /**
    * 会员回传接口，当非码回传会员的时候，需要调用该接口告知订单会员ID
    **/
    void updateOrderMemberId(1: order_request_new.UpdateOrderMemberIdRequest request)

    /**
    * 取消订单的sku,新增返回结果
    **/
    order_response_new.TCancelOrderSkuResponse2New cancelOrderSkuNew(1: order_request_new.TCancelOrderSkuRequestNew request)
    (events="com.today.api.order.events.PartRefundEventNew")

    order_response_new.TCancelOrderResponseNew cancelOrderNew(order_request_new.TCancelOrderRequest request)
    (events="com.today.api.order.events.CancelOrderEventNew")

    /**
    *  取消订单所有支付，但是不取消订单
    **/
    order_response_new.TCancelWholeOrderPaymentsResponse cancelWholeOrderPaymentsNew(1: string orderNo)

    /**
    *   取消订单所有支付，但是不取消订单(兼容老版本)
    **/
    order_response_new.TCancelWholeOrderPaymentsResponse cancelWholeOrderPayments2New(1: order_request_new.CancelWholeOrderPaymentsRequest request)

    order_response_new.TReversePaymentResponse reverseOrderPayment(1: order_request_new.TReversePaymentRequest request)


    /**
    * 批量创建线下的订单支付 (包含支付跟退款流水)
    * 业务场景：
    * 在pos上进行的非移动支付，会员支付，代金券支付的离线支付（现金，武汉通，银联卡）等支付后，批量把支付流水抛过来，因为可能该订单存在移动支付，所以抛过来有可能是后置
    * 1.取出该订单下面所有的支付流水，并判断该订单状态是否为未完成以及已退款，如果是，则抛出异常，只能是未完成或者部分付款的状态
    * 先判断订单状态如果为已完成，request的金额必须为0
    * 2.判断该订单下面所属所有的支付流水，并判断request的金额+流水总额相加==订单实付金额，需要注意的是request的金额必须是正负流水相加
    * 3.批量insert之后回写订单状态为已完成
    **/
    void createDoneOrderPayments(1: list<order_request_new.TCreateOrderPaymentRequestNew> request)
    (events="com.today.api.order.events.StoreOrderEndEventNew")

    /**
    * 批量创建取消订单的支付流水(包含支付跟退款流水)
    * 只可能在 部分付款，未付款状态调用该接口
    *
    * 调用该接口后， 订单一定为取消交易
    * 批量创建取消订单支付 (只包含退款流水)
    * 业务场景：
    * 在pos上进行的批量退款，可能存在网络断开情况导致武汉通，银联支付退款，同上的后发先置
    * 1.前置检查：只有未完成的订单以及部分付款才能调用
    * 2.判断不能超退：逻辑同上，并且必须全部退完
    * 3.回写order的订单状态
    **/
    void createCanceledOrderPayments(1: list<order_request_new.TCreateOrderPaymentRequestNew> request)

    /**
    * 会员回传接口，当非码回传会员的时候，需要调用该接口告知订单会员ID
    **/
    list<i64> createMiniOrders(1: list<order_request_new.TCreateMiniOrderRequest> request)
}(group="Order")