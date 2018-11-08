namespace java com.today.api.order.vo

include 'order_enum.thrift'

/**
* 订单优惠劵
**/
struct TOrderCouponNew {
    /**
    * 订单号
    **/
    1: string orderNo,
    /**
    * 优惠券id
    **/
    2: i64 couponId,
    /**
    * 优惠劵类型,1:商品券(PRODUCT_COUPON);2:现金券(CASH_COUPON)
    **/
    3: order_enum.CouponTypeEnum couponType,
    /**
    * 优惠券key
    **/
    4: string couponKey,
}

/**
* 订单主体
**/
struct TOrderNew {
    /**
    * 订单id
    **/
    1: i64 id,
    /**
    * 订单编号
    **/
    2: string orderNo,
    /**
    * 订单状态
    **/
    3: order_enum.OrderStatusEnum orderStatus,
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
    * 优惠/折扣金额
    * @datatype(name="bigdecimal")
    **/
    10: optional double discountAmount,
    /**
    * 退款总金额
    * @datatype(name="bigdecimal")
    **/
    25: optional double refundAmount,
    /**
    * 实际折扣金额
    * @datatype(name="bigdecimal")
    **/
    26: optional double actualDiscountAmount,
    /**
    * POS机编号
    **/
    7: string posId,
    /**
    * 门店编号
    **/
    8: string storeId,
    /**
    * 会员id
    **/
    9: optional i64 memberId,
    /**
    * 优惠券id,以json数组形式存储
    **/
    11: optional string coupons,
    /**
    * 支付时间
    **/
    12: optional i64 payTime,
    /**
    * 用户手机号
    **/
    14: optional string mobile,
    /**
    * 订单备注
    **/
    15: optional string remark,
    /**
    * 订单完成时间
    **/
    16: optional i64 finishTime,
    /**
    * 创建人
    **/
    17: i32 createdBy,
    /**
    * 创建时间
    **/
    18: i64 createdAt,
    /**
    * 修改人
     **/
    19: i32 updatedBy,
    /**
    * 修改时间
    **/
    20: i64 updatedAt,
    /**
    * 收银员
    **/
    21: i32 cashierId
    /**
    * 微信openId
    **/
    22: optional string wechatOpenId
    /**
    * 支付宝openId
    **/
    23: optional string alipayOpenId
    /**
    * 已支付金额
    * @datatype(name="bigdecimal")
    **/
    24: optional double paidAmount
}

/**
* 订单详情
**/
struct TOrderDetailNew {

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

}

/**
* 订单支付信息
**/
struct TOrderPaymentNew {
    /**
    * 根据取号服务获取Id
    **/
    1: i64 id,
    /**
    * 订单号，唯一
    **/
    2: string orderNo,
    /**
    * 支付顺序号,在同一一个订单中顺序递增,由前端生成,防止止重复提交, 同一订单内唯一
    **/
    3: i32 paySeq,
    /**
    * 支付请求端标识: 如果是微信/支付宝: 支付条形码, 优惠券对应券Id， 现金支付不需要填
    **/
    4: optional string payRequestId,
    /**
    *  第三方支付流水号
    **/
    5: optional string thirdPartyPaymentNo,
    /**
    *  对退款,原支付流水记录id
    **/
    6: optional i64 originPaymentId,
    /**
    * 支支付渠道,1:支支付宝(ALI_PAY);2:微信
    * (WECHAT_PAY);3:银行行行卡(BANK_CARD);4:账户(ACCOUNT);5:优惠券(COUPON);6:现金金金(CASH);7:移
    * 动支支付(MOBILE_PAY);8:其他(OTHER);9:京东到家(JINGDONGDAOJIA);10:饿了么(ELEME); 11: 美团(MEITUAN); 12 城市一卡通 (ONECARD)
    **/
    7: order_enum.TradeTypeEnum tradeType,
    /**
    * 支付金额
    * @datatype(name="bigdecimal")
    **/
    8: double tradeAmount,
    /**
    * 支付发起时间(POS端)
    **/
    9: i64 payTime,
    /**
    * 状态,1:未支支付(NON_PAYMENT);2:已
    *  支支付(PAID);4:已退款(REFUNDED);5:超时(TIMEOUT);6:失败(FAILED);
    *
    *  3退款中(REFUNDING)废弃
    **/
    10: order_enum.PayStatusEnum payStatus,
    /**
    * 支付类型,1:支付(PAY);2:退款(REFUND);
    **/
    11: order_enum.PaymentTypeEnum paymentType,
    /**
    * 第三方返回字段: 可以由OpenId判断是否是会员，再通过会员服务可获取memberId
    **/
    12: optional string openId
    /**
    * 备注
    **/
    20: optional string remark,
    /**
    *创建人
    **/
    21: i32 createdBy,
    /**
    *创建时间
    **/
    22: i64 createdAt,
    /**
    * 修改人
    **/
    23: i32 updatedBy,
    /**
    * 修改时间
    **/
    24: i64 updatedAt,
    /**
    * 第三方交易Id, 对接非码用
    **/
    25: optional string payTransId
}

/**
* 订单退款流水信息(区分支付时退款与订单完成后退款)
**/
struct TOrderPaymentRefund {
    /**
    * 根据取号服务获取Id
    **/
    1: i64 id,
    /**
    * 订单号，唯一
    **/
    2: string orderNo,
    /**
    * 支付顺序号,在同一一个订单中顺序递增,由前端生成,防止止重复提交, 同一订单内唯一
    **/
    3: i32 paySeq,
    /**
    * 支付请求端标识: 如果是微信/支付宝: 支付条形码, 优惠券对应券Id， 现金支付不需要填
    **/
    4: optional string payRequestId,
    /**
    *  第三方支付流水号
    **/
    5: optional string thirdPartyPaymentNo,
    /**
    *  对退款,原支付流水记录id
    **/
    6: optional i64 originPaymentId,
    /**
    * 支支付渠道,1:支支付宝(ALI_PAY);2:微信
    * (WECHAT_PAY);3:银行行行卡(BANK_CARD);4:账户(ACCOUNT);5:优惠券(COUPON);6:现金金金(CASH);7:移
    * 动支支付(MOBILE_PAY);8:其他(OTHER);9:京东到家(JINGDONGDAOJIA);10:饿了么(ELEME); 11: 美团(MEITUAN); 12 城市一卡通 (ONECARD)
    **/
    7: order_enum.TradeTypeEnum tradeType,
    /**
    * 支付金额
    * @datatype(name="bigdecimal")
    **/
    8: double tradeAmount,
    /**
    * 支付发起时间(POS端)
    **/
    9: i64 payTime,
    /**
    * 状态,1:未支支付(NON_PAYMENT);2:已
    *  支支付(PAID);4:已退款(REFUNDED);5:超时(TIMEOUT);6:失败(FAILED);
    *
    *  3退款中(REFUNDING)废弃
    **/
    10: order_enum.PayStatusEnum payStatus,
    /**
    * 支付类型,1:支付(PAY);2:退款(REFUND);
    **/
    11: order_enum.PaymentTypeEnum paymentType,
    /**
    * 第三方返回字段: 可以由OpenId判断是否是会员，再通过会员服务可获取memberId
    **/
    12: optional string openId
    /**
    * 备注
    **/
    20: optional string remark,
    /**
    *创建人
    **/
    21: i32 createdBy,
    /**
    *创建时间
    **/
    22: i64 createdAt,
    /**
    * 修改人
    **/
    23: i32 updatedBy,
    /**
    * 修改时间
    **/
    24: i64 updatedAt,
    /**
    * 第三方交易Id, 对接非码用
    **/
    25: optional string payTransId,
    /**
    *支付退款流水类型(0:订单完成后退款流水,1:订单支付时退款流水)
    **/
    26:optional order_enum.RefundStyleEnum refundStyle,
    /**
    *原支付流水对应的退款序号
    **/
    27:optional i32 originPaySeq
}

/**
* 订单查询主体
**/
struct TOrderItemNew {
    /**
    * 订单id
    **/
    1: i64 id,
    /**
    * 订单编号
    **/
    2: string orderNo,
    /**
    * 订单状态
    **/
    3: order_enum.OrderStatusEnum orderStatus,
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
    * 优惠/折扣金额
    * @datatype(name="bigdecimal")
    **/
    7: optional double discountAmount,
    /**
    * 退款总金额
    * @datatype(name="bigdecimal")
    **/
    8: optional double refundAmount,
    /**
    * 实际折扣金额
    * @datatype(name="bigdecimal")
    **/
    9: optional double actualDiscountAmount,
    /**
    * POS机编号
    **/
    10: string posId,
    /**
    * 门店编号
    **/
    11: string storeId,
    /**
    * 会员id
    **/
    12: optional i64 memberId,
    /**
    * 优惠券id,以json数组形式存储
    **/
    13: optional string coupons,
    /**
    * 支付时间
    **/
    14: optional i64 payTime,
    /**
    * 下单时间
    **/
    15: i64 createTime,
    /**
    * 用户手机号
    **/
    16: optional string mobile,
    /**
    * 订单备注
    **/
    17: optional string remark,
    /**
    * 订单完成时间
    **/
    18: optional i64 finishTime,
    /**
    * 创建人
    **/
    19: i32 createdBy,
    /**
    * 创建时间
    **/
    20: i64 createdAt,
    /**
    * 修改人
     **/
    21: i32 updatedBy,
    /**
    * 修改时间
    **/
    22: i64 updatedAt,
    /**
    * 收银员
    **/
    23: i32 cashierId
    /**
    * 微信openId
    **/
    24: optional string wechatOpenId
    /**
    * 支付宝openId
    **/
    25: optional string alipayOpenId
    /**
    * 已支付金额
    * @datatype(name="bigdecimal")
    **/
    26: optional double paidAmount
    /**
    * 门店名称
    **/
    27: optional string storeName
    /**
    * 业务类型:1-pos订单，2-mini货架订单，3-预定自提订单，4-扫码购物订单，5-样品订单
    **/
    28: optional order_enum.OrderTypeEnum orderType
    /**
    * 支付方式
    **/
    29: list<order_enum.TradeTypeEnum> orderTradeTypes
    /**
    * 抹零金额
    **/
    30: optional double spotAmount
}

/**
* 订单优惠劵详情
**/
struct TOrderCouponItemNew {
    /**
    * 订单号
    **/
    1: string orderNo,
    /**
    * 优惠券id
    **/
    2: i64 couponId,
    /**
    * 优惠劵类型,1:商品券(PRODUCT_COUPON);2:现金券(CASH_COUPON)
    **/
    3: order_enum.CouponTypeEnum couponType,
    /**
    * 优惠券key
    **/
    4: string couponKey,
    /**
    * 优惠商品货号
    **/
    5: optional string skuNo,
    /**
    * 优惠价格
    * @datatype(name="bigdecimal")
    **/
    6: optional double discountAmount
}
