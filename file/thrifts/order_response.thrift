namespace java com.today.api.order.response

include 'order_enum.thrift'
include 'order_vo.thrift'
include 'page.thrift'


struct TOrderRefundResponse {

}



struct TOrderCommonResponse {
    1: string responseCode,
    2: string responseMsg
}

/**
* 返回订单列表
**/
struct TQueryOrderListResponse {
    /**
    *  order列表
    **/
    1: list<order_vo.TOrder> orders

   /**
            * 分页
            **/
    2: page.TPageResponse pageResponse,

}
/**
* 返回订单支付列表
**/
struct TOrderPaymentListResponse {
    1: list<order_vo.TOrderPayment> orderPayments
}


/**
* 订单支付返回
**/
struct TOrderPaymentResponse {
    /**
    * 订单号
    **/
    1: string orderNo,
    /**
    * 第三方支付流水
    **/
    2: string thirdPartyPaymentNo
    /**
    * 支付渠道
    **/
    3: order_enum.TradeTypeEnum tradeType,
    /**
    * 支付id
    **/
    4: i64 payId
}

/**
* 查询订单营收总数
**/
struct TQueryOrderPaymentSumListResponse{
    /**
    * 查询营销总数
    **/
    1: double sumPayment,
}


/**
*   查询订单详情返回
**/
struct TQueryOrderDetailResponse{
    /**
    *  order列表
    **/
    1: order_vo.TOrder orders,

    /**
    *支付列表
    **/
    2: list<order_vo.TOrderPayment> orderPayments,
      /**
        *订单商品列表
        **/
    3: list<order_vo.TOrderDetail> orderDetail,
     /**
      *优惠劵列表
      **/
    4: list<order_vo.TOrderCoupon> orderCoupon,



}

struct ListOrderDetailByIdsResponse {
    1: list<order_vo.TOrderDetail> orderDetailList
}


