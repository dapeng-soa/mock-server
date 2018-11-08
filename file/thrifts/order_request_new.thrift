namespace java com.today.api.order.request

include 'order_enum.thrift'
include 'order_vo_new.thrift'
include 'page.thrift'

/**
* 支付流水请求体
**/
struct TCreateOrderPaymentRequestNew {
    /**
    * 订单号
    **/
    1: string orderNo,
    /**
    * 支付顺序号,在同一一个订单中顺序递增,由前端生成,防止止重复提交, 同一订单内唯一
    **/
    2: i32 paySeq,
    /**
    * 支付渠道,1:支付宝(ALI_PAY);2:微信(WECHAT_PAY);3:银行卡(BANK_CARD);4:账户(ACCOUNT);5:优惠券(COUPON);6:其他(OTHER);
    **/
    4: order_enum.TradeTypeEnum tradeType,
    /**
    * 支付金额
    * @datatype(name="bigdecimal")
    **/
    5: double tradeAmount,
    /**
    * 支付发起时间
    **/
    6: i64 payTime,
    /**
    * 状态,1:未支付(NON_PAYMENT);2:已支付(PAID);3:退款中(REFUNDING);4:已退款(REFUNDED);5:超时(TIMEOUT);6:失败(FAILED);
    **/
    7: order_enum.PayStatusEnum payStatus,
    /**
    * 支付类型，1:支付(PAY);2:退款(REFUND);
    **/
    8: order_enum.PaymentTypeEnum paymentType,
    /**
    * 备注
    **/
    9: optional string remark,
    /**
    * 会员id
    **/
    10: optional i64 memberId,
    /**
    *  支付请求支付code
    *  1. 现金支付不需要
    *  2. 会员支付，则需要传 会员码
    *  3. 支付宝/微信， 传条形交易码
    *  4. 优惠券对应券Id
    **/
    11: optional string payRequestId,

    /**
    *  抹零垫付类型  1: 抹零支付(payment_spot); 2: 退款垫付(refund_spot)
    **/
    12: optional order_enum.SpotTypeEnum spotType,
   /**
   * 垫付金额
   * @datatype(name="bigdecimal")
   **/
    13: optional double spotAmount
   /**
   * 如果该笔流水为退款流水，这里就表示该笔流水的支付序号
   **/
    14:optional i32 originPaySeq
    /**
    * 支付备注，使用于武汉通，银联支付信息POS上送
    **/
    15: optional string payRemark
    /**
    * 门店所属区域代码 武汉(4201) ,长沙(4301) , 南宁()
    **/
    16: optional string storeAreaCode

}

/**
* 订单详情请求体
**/
struct TCreateOrderDetailRequestNew {
    /**
    * 订单编号
    **/
    1: string orderNo,
    /**
     * 商品skuNo
    **/
    2: string skuNo,
    /**
    * 订单详情行号, 针对同一Sku存在多行的情况设计
    *
    **/
    3: i32 detailSeq,
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
    6: string barcode,
    /**
    *  商品行使用的优惠券: 用于支持退某商品时退券的处理.
    **/
    7: i64 couponId,
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
     * 小计金额
     * @datatype(name="bigdecimal")
    **/
    12: double skuAmount,
    /**
    * sku 售价金额/单价金额
    * @datatype(name="bigdecimal")
    **/
    13: double skuSellingPrice,
    /**
     * 备注
    **/
    14: optional string remark,
    /**
    * 商品促销id
    **/
    15: optional i64 promotionId

}

/**
* 创建订单请求体
* 创建在线订单应该不包含 支付信息，优惠券信息
* 故： paymentList, coupons 字段已删除
**/
struct TCreateOrderRequestNew {
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
    * @datatype(name="bigdecimal")
    **/
    5: double orderTotalAmount,
    /**
    * 订单实际付款金额
    * @datatype(name="bigdecimal")
    **/
    6: double orderActualAmount,
    /**
    * POS机编号
    **/
    7:  i32 posId,
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
    * @datatype(name="bigdecimal")
    **/
    10: optional double discountAmount,
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
    *订单详情
    **/
    17: list<TCreateOrderDetailRequestNew> orderDetails,
    /**
    * 订单优惠券
    **/
    18: list<order_vo_new.TOrderCouponNew> orderCoupons,
    /**
    * 支付信息
    **/
    19: list<TCreateOrderPaymentRequestNew> payments,
    /**
    * 门店所属区域代码 武汉(4201) ,长沙(4301) , 南宁()
    **/
    20: optional string storeAreaCode
}

/**
*    根据商品退款的请求体
**/
struct TCancelOrderSkuRequestNew {
    /**
    * 订单编号
    **/
    1: string orderNo,
    /**
    * 商品skuNo
    **/
    2: string skuNo,
    /**
    * 商品数量
    **/
    3: i32 count,
    /**
    * 商品行号
    **/
    4: i32 detailSeq,
    /**
    * 小计金额
    * @datatype(name="bigdecimal")
    **/
    5: double skuAmount

    /**
    * 折扣金额
    * @datatype(name="bigdecimal")
    **/
    6:double disCountAmount;
    /**
    *  抹零垫付类型  1: 抹零支付(payment_spot); 2: 退款垫付(refund_spot)
    **/
    7: optional order_enum.SpotTypeEnum spotType,
    /**
    *  垫付金额
    *  @datatype(name="bigdecimal")
    **/
    8: optional double spotAmount,
    /**
    * 门店所属区域代码 武汉(4201) ,长沙(4301) , 南宁()
    **/
    9: optional string storeAreaCode

}

/**
* 支付取消请求体
**/
struct TCancelOrderPaymentRequest {
    /**
    * 订单编号
    **/
    1: string orderNo,
  /**
    * 当前取消支付Id
    **/
   2: i64 payId,
   /**
     * 支付顺序号,在同一一个订单中顺序递增,由前端生成,防止止重复提交, 同一订单内唯一
    **/
   3: i32 paySeq,
   /**
    * 门店所属区域代码 武汉(4201) ,长沙(4301) , 南宁()
    **/
   4: optional string storeAreaCode
}

/**
* 订单取消实体
**/
struct TCancelOrderRequest {
    1: string orderNo,
    /**
    * 门店所属区域代码 武汉(4201) ,长沙(4301) , 南宁()
    **/
    2: optional string storeAreaCode
}

/**
*  根据订单状态，支付渠道，支付类型，订单编号，门店编号，pos编号，会员手机号，支付流水号，下单时间，支付时间进行查询
**/
struct TQueryOrderListRequestNew{
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
    * 业务类型:mini货架、预定自提、扫码购物、样品等...
    **/
    13:optional order_enum.OrderTypeEnum orderType,
    /**
     *分页
    **/
    14:page.TPageRequest pageRequest,
    /**
    * 退款状态 0:未退款(NON_REFUND),1:部分退款(PART_REFUND);2:已退款(REFUNDED)
    **/
    15:optional order_enum.OrderRefundEnum orderRefundType,
    /**
    * 收银员id
    **/
    16:optional i64 cashierId,
    /**
    * 商品名称或者货号
    **/
    17:optional string skuInfo,
}

/**
* 根据店铺id、pos机id统计当天的营收总金额
**/
struct TQueryOrderPaymentSumListRequestNew{
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
struct ListDoneOrderDetailRequestNew{
    /**
    *门店编号
    **/
    1:string storeId,
    /**
    *起始时间
    **/
    2:i64 startTime,
    /**
    *结束时间
    **/
    3:i64 endTime
}

struct UpdateOrderMemberIdRequest{
    1: string transId,
    2: i64 memberId
}


//////////////////////////////-------------------------////////////////////////-------------------
/**
* 翼码支付绑定终端
**/
struct TBindPosRequest {
        /**
         * 终端号
         */
        1: string posId,

        2: string userId,

        /**
         * 门店编号
         */
        3: string storeId,
        /**
        * 门店名
        **/
        4: string storeName,
        /**
        * 门店所属区域代码 武汉(4201) ,长沙(4301) , 南宁()
        **/
        5: optional string storeAreaCode
}









struct CancelWholeOrderPaymentsRequest {
    /**
    * 订单号
    **/
    1: string orderNo,
    /**
    *  抹零垫付类型  1: 抹零支付(payment_spot); 2: 退款垫付(refund_spot)
    **/
    12: optional order_enum.SpotTypeEnum spotType,
    /**
    *  垫付金额
    *  @datatype(name="bigdecimal")
    **/
    13: optional double spotAmount,
    /**
    * 门店所属区域代码 武汉(4201) ,长沙(4301) , 南宁()
    **/
    14: optional string storeAreaCode
}

struct TReversePaymentRequest {
    /**
    * 订单号
    **/
    1: string orderNo,
    /**
    * 支付序号
    **/
    2: i32 paySeq
}

/**
* mini货架订单创建请求体
**/
struct TCreateMiniOrderRequest {
    /**
    * 订单编号
    **/
    1: string orderNo,
    /**
    *  商品项目数量，不是商品
    **/
    2: i32 productCount,
    /**
    * 订单总金额
    * @datatype(name="bigdecimal")
    **/
    3: double orderTotalAmount,
    /**
    * 订单实际付款金额
    * @datatype(name="bigdecimal")
    **/
    4: double orderActualAmount,
    /**
    * POS机编号
    **/
    5: i32 posId,
    /**
    * 门店编号
    **/
    6: string storeId,
    /**
    * 会员id
    **/
    7: optional i64 memberId,
    /**
    * 优惠/折扣金额
    * @datatype(name="bigdecimal")
    **/
    8: optional double discountAmount,
    /**
    * 用户手机号
    **/
    9: optional string mobile,
    /**
    * 订单备注
    **/
    10: optional string remark,
    /**
    * 下单时间
    **/
    11: i64 createTime,
    /**
    * 收银员
    **/
    12: i64 cashierId,
    /**
    * 订单类型
    **/
    13: order_enum.OrderTypeEnum orderType,
    /**
    *订单详情
    **/
    14: list<TCreateOrderDetailRequestNew> orderDetails,
    /**
    * 支付信息
    **/
    15: list<TCreateOrderPaymentRequestNew> payments,
    /**
    * 支付时间
    **/
    16: i64 payTime,
    /**
    * 完成时间
    **/
    17: i64 finishTime
}