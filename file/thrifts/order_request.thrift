namespace java com.today.api.order.request

include 'order_enum.thrift'
include 'order_vo.thrift'
include 'page.thrift'



/**
* 支付流水请求体
**/
struct TOrderPaymentRequest {
    /**
    * 订单号
    **/
    1: string orderNo,
    /**
    * 第三方支付码
    **/
    2: optional string thirdPartyPaymentCode,
    /**
    * 支付渠道,1:支付宝(ALI_PAY);2:微信(WECHAT_PAY);3:银行卡(BANK_CARD);4:账户(ACCOUNT);5:优惠券(COUPON);6:其他(OTHER);
    **/
    3: order_enum.TradeTypeEnum tradeType,
    /**
    * 支付金额
    **/
    4: double tradeAmount,
    /**
    * 支付发起时间
    **/
    5: i64 payTime,
    /**
    * 支付状态，1:未支付(NON_PAYMENT);2:已支付(PAID);3:退款中(REFUNDING);4:已退款(REFUNDED);
    **/
    6: order_enum.PayStatusEnum payStatus,
    /**
    * 支付类型，1:支付(PAY);2:退款(REFUND);
    **/
    7: order_enum.PaymentTypeEnum paymentType,
    /**
    * 备注
    **/
    8: optional string remark,
    /**
    * 会员id
    **/
    9: optional i64 memberId,

}

struct TPaymentRequest {
    /**
    * 订单号
    **/
    1: string orderNo,


    }

/**
* 订单详情请求体
**/
struct TOrderDetailRequest {
    /**
    * 订单编号
    **/
    2: string orderNo,
    /**
     * 商品skuNo
    **/
    3: string skuNo,

    /**
     * 商品名称
    **/
    4: string skuName,
    /**
    * sku版本
    **/
    5: i32 skuVersion,
    /**
     * 商品条码(pos机扫的条码)
    **/
    7: string barcode,

    /**
     * 优惠金额
    **/
    10: optional double discountAmount,
    /**
     * 购买数量
    **/
    11: i32 skuCount,
    /**
     * 小计金额
    **/
    12: double skuAmount,
    /**
     * 备注
    **/
    13: optional string remark,

}


/**
* 创建订单请求体
**/
struct TOrderRequest {
    /**
    * 订单编号
    **/
    2: string orderNo,

    /**
    *  商品项目数量，不是商品
    **/
    4: i32 productCount,

    /**
    * 订单总金额
    **/
    5: double orderTotalAmount,
    /**
    * 订单实际付款金额
    **/
    6: double orderActualAmount,
    /**
    * POS机编号
    **/
    7:  string posId,
    /**
    * 门店编号
    **/
    8:  string storeId,
    /**
    * 会员id
    **/
    9: optional i64 memberId,
    /**
    * 优惠/折扣金额
    **/
    10: double discountAmount,
    /**
    * 用户手机号
    **/
    11: optional string mobile,
    /**
    * 订单备注
    **/
    12: optional string remark,
    /**
    * 下单时间
    **/
    13: i64 createTime,
     /**
        * 收银员
        **/
    14: i64 cashierId,

        /**
        * 门店名称
        **/
    15: string storeName,

    /**
    * 支付信息
    **/
    16: list<TOrderPaymentRequest> payments,
    /**
    *订单详情
    **/
    17: list<TOrderDetailRequest> orderDetails,
    /**
        * 优惠券
        **/
    18: list<order_vo.TOrderCoupon> coupons,

}
/**
* 支付取消请求体
**/
struct TOrderPaymentCancelRequest {
    1: string orderNo,
    /**
   * 第三方支付流水号
   **/
   2: string thirdPartyPaymentNo,
   /**
   * 支付渠道,1:支付宝(ALI_PAY);2:微信(WECHAT_PAY);3:银行卡(BANK_CARD);4:账户(ACCOUNT);5:积分(SCORE);6:优惠券(COUPON);7:其他(OTHER);
   **/
   3: order_enum.TradeTypeEnum tradeType,
   /**
   *取消金额
   **/
   4: double tradeAmount,
   /**
   *取消id
   **/
   5: i64 payId

}

/**
* 订单取消实体
**/
struct TOrderCancelRequest {
    1: string orderNo
}

/**
*  根据订单状态，支付渠道，支付类型，订单编号，门店编号，pos编号，会员手机号，支付流水号，下单时间，支付时间进行查询
**/
struct TQueryOrderListRequest{
    /**
    *pos号
    **/
    1:optional string posId,
    /**
    *下单开始时间
    **/
    2:optional i64 startTime,
    /**
    *下单结束时间
    **/
    3:optional i64 endTime,
    /**
      *订单状态
     **/
    4:optional order_enum.OrderStatusEnum orderStatus,
    /**
      *支付渠道
    **/
    5:optional order_enum.TradeTypeEnum tradeType,
    /**
      *支付类型
     **/
    6:optional order_enum.PaymentTypeEnum paymentType,
    /**
      *订单编号
      **/
    7:optional string orderNo,
    /**
      *门店编号
      **/
    8:optional string storeId,
    /**
      *手机号
      **/
    9:optional string mobile,
    /**
      *支付流水
      **/
    10:optional string thirdPartyPaymentNo,
    /**
      *支付开始时间
      **/
    11:optional i64 payOrderStart,
    /**
      *支付结束时间
      **/
    12:optional i64 payOrderEnd,

    /**
     *分页
    **/
    13: page.TPageRequest pageRequest,

}


/**
* 订单详情请求接口
**/
struct TQueryOrderDeatilRequest{
    /**
    * 订单id
    **/
    1: i64 orderId
}

/**
* 查询该订单下的所有支付体
**/
struct TOrderPaymentListRequest {
    /**
    * 订单号
    **/
    1: string orderNo
}
/**
* 查询该订单下的所有支付体
**/
struct ListOrderDetailByIdsRequest {
    /**
    * 订单详情Id
    **/
    1: list<i64> orderDetailIds
}


/**
*    根据商品退款的请求体
**/
struct TOrderSkuRequest {
    1: string orderNo,
    /**
    * 商品skuNo
    **/
    2: string skuNo,
    /**
    * 小计金额
    **/
    3: double skuAmount
}

/**
* 根据店铺id、pos机id统计当天的营收总金额
**/
struct TQueryOrderPaymentSumListRequest{
  /**
    *pos号
    **/
    1:string posId,
    /**
    *开始时间
    **/
    2:i64 startTime,
    /**
    *结束时间
    **/
    3:i64 endTime,

}

/**
* 根据门店编号、起始时间、结束时间查询商品订单详情列表
**/
struct ListDoneOrderDetailRequest{
    /**
    *门店编号
    **/
    1:string storeCode,
    /**
    *起始时间
    **/
    2:i64 startTime,
    /**
    *结束时间
    **/
    3:i64 endTime
}

/**
* 5/14新增创建离线订单请求体
**/
struct TOfflineOrderRequest {
    /**
    * 订单编号
    **/
    2: string orderNo,
    /**
    *  商品项目数量，不是商品
    **/
    4: i32 productCount,
    /**
    * 订单总金额
    **/
    5: double orderTotalAmount,
    /**
    * 订单实际付款金额
    **/
    6: double orderActualAmount,
    /**
    * POS机编号
    **/
    7:  string posId,
    /**
    * 门店编号
    **/
    8:  string storeId,
    /**
    * 会员id
    **/
    9: optional i64 memberId,
    /**
    * 优惠/折扣金额
    **/
    10: double discountAmount,
    /**
    * 用户手机号
    **/
    11: optional string mobile,
    /**
    * 订单备注
    **/
    12: optional string remark,
    /**
    * 下单时间
    **/
    13: i64 createTime,
    /**
    * 收银员
    **/
    14: i64 cashierId,
    /**
    * 门店名称
    **/
    15: string storeName,
    /**
    * 支付信息
    **/
    16: list<TOrderPaymentRequest> payments,
    /**
    *订单详情
    **/
    17: list<TOrderDetailRequest> orderDetails,
    /**
    *订单状态
    **/
    18: order_enum.OrderStatusEnum orderStatus,
}
