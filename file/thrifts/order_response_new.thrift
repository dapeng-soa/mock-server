namespace java com.today.api.order.response

include 'order_enum.thrift'
include 'order_vo_new.thrift'
include 'page.thrift'

/**
* 订单支付返回
**/
struct TOrderPaymentResponseNew {
    /**
    * 订单号
    **/
    1: string orderNo,
    /**
    * 第三方支付流水(现金，优惠券没有第三方支付流水号)
    **/
    2: optional string thirdPartyPaymentNo
    /**
    * 支付渠道
    **/
    3: order_enum.TradeTypeEnum tradeType,
    /**
    * 支付id
    **/
    4: i64 payId,
    /**
    * 支付结果码
    **/
    5: order_enum.TradeResultCode tradeResultCode
}

/**
* 返回订单列表
**/
struct TQueryOrderListResponseNew {
    /**
    *  order列表
    **/
    1: list<order_vo_new.TOrderItemNew> orders

   /**
            * 分页
            **/
    2: page.TPageResponse pageResponse,

}

/**
* 返回订单支付列表
**/
struct TOrderPaymentListResponseNew {
    1: list<order_vo_new.TOrderPaymentNew> orderPayments
}

/**
* 返回取消商品response
**/
struct TCancelOrderSkuResponseNew {
     /**
       * 原支付的第三方支付流水号 (不传)
       **/
       1:optional string thirdPartyPaymentNo,
        /**
        * 支付顺序号,在同一一个订单中顺序递增,取消商品返回seq给前端
        **/
       2:i32 paySeq
}

/**
* 查询订单营收总数
**/
struct TQueryOrderPaymentSumListResponseNew {
    /**
    * 查询营销总数
    * @datatype(name="bigdecimal")
    **/
    1: double sumPayment,
}

/**
*   订单详情详细列表
**/
struct OrderDetailItem{
    /**
    * 订单ID
    **/
    1: i64 id,
    /**
    * 订单编号
    **/
    2: string orderNo,
    /**
     * 商品No
    **/
    3: string skuNo,
    /**
    *  商品行号，针对同一订单存在多个一致的sku的情况
    *    如: sku1 1 ￥4
    *        sku1 2 ￥2
    **/
    4: i32 detailSeq,
    /**
     * 商品名称
    **/
    5: string skuName,
    /**
    * sku版本
    **/
    6: i64 skuVersion,
    /**
     * 商品条码(pos机扫的条码)
    **/
    7: string barcode,
    /**
     * 优惠金额
     * @datatype(name="bigdecimal")
    **/
    10: optional double discountAmount,
    /**
     * 购买数量
    **/
    11: i32 skuCount,
    /**
     * sku售价金额
     * @datatype(name="bigdecimal")
    **/
    12: double skuSellingPrice,
    /**
     * 备注
    **/
    13: optional string remark,
    /**
     * 商品状态
    **/
    14: i32 skuStatus,
    /**
    * Sku退货数量
    **/
    15: optional i32 refundCount,
    /**
     * sku实际折扣金额
     * @datatype(name="bigdecimal")
    **/
    16: optional double actualDiscountAmount,
    /**
    *  商品行使用的优惠券: 用于支持退某商品时退券的处理.
    **/
    17: i64 couponId
    /**
     * 计价单位
    **/
    18: string saleUnit,
    /**
     * 促销id
    **/
    19: optional i64 promotionId,
}

/**
*   查询订单详情返回
**/
struct TGetOrderSummaryResponseNew{
    /**
    *  order列表
    **/
    1: order_vo_new.TOrderItemNew orders,

    /**
    *支付列表
    **/
    2: list<order_vo_new.TOrderPaymentNew> orderPayments,
      /**
        *订单商品列表
        **/
    3: list<OrderDetailItem> orderDetail,
     /**
      *优惠劵列表
      **/
    4: list<order_vo_new.TOrderCouponItemNew> orderCoupon,



}


/**
* wrap event need metadata,包裹元数据信息
**/
struct TOrderAndOrderDetailResponse {

    1: order_vo_new.TOrderNew order,

    2: list<order_vo_new.TOrderDetailNew> orderDetail

}

/**
* 返回取消商品response
**/
struct TCancelOrderSkuResponse2New {
     /**
       * 原支付的第三方支付流水号 (不传)
       **/
       1:optional string thirdPartyPaymentNo,
        /**
        * 支付顺序号,在同一一个订单中顺序递增,取消商品返回seq给前端
        **/
       2:i32 paySeq,
       3:order_vo_new.TOrderPaymentNew refundPayment
}

struct TCancelOrderResponseNew {
       1: list<order_vo_new.TOrderPaymentNew> refundPayments,
}

struct TCancelWholeOrderPaymentsResponse{
        1: list<order_vo_new.TOrderPaymentNew> refundPayments,
}

struct TReversePaymentResponse {
/**
    * 订单号
    **/
    1: string orderNo,
    /**
    * 第三方支付流水(现金，优惠券没有第三方支付流水号)
    **/
    2: optional string thirdPartyPaymentNo
    /**
    * 支付渠道
    **/
    3: order_enum.TradeTypeEnum tradeType,
    /**
    * 支付id
    **/
    4: i64 payId,
    /**
    * 支付结果码
    **/
    5: order_enum.TradeResultCode tradeResultCode
}