namespace java com.today.api.order.vo

include 'order_enum.thrift'


/**
* 订单优惠劵
**/
struct TOrderCoupon {
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
    * skuNo
    **/
    4: optional string skuNo,
    /**
    *  优惠金额
    **/
    5: optional double discountPrice
}

/**
* 订单主体
**/
struct TOrder {
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
    **/
    5: double orderTotalAmount,
    /**
    * 订单实际付款金额
    **/
    6: double orderActualAmount,
    /**
    * POS机编号
    **/
    7: optional string posId,
    /**
    * 门店编号
    **/
    8: optional string storeId,
    /**
    * 会员id
    **/
    9: optional i32 memberId,
    /**
    * 优惠/折扣金额
    **/
    10: double discountAmount,
    /**
    * 优惠券id,以json数组形式存储
    **/
    11: string coupons,
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
    **/
    24: optional double paidAmount
}


/**
* 订单详情
**/
struct TOrderDetail {

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

    /**
     * 商品状态
    **/
    14: i32 skuStatus,
    /**
     * 门店售价
    **/
    15: optional double sellingPrice,

    /**
     * 计价单位
    **/
    16: optional string saleUnit,
}

/**
* 订单支付信息
**/
struct TOrderPayment {
    1: i64 id,
    /**
    * 订单号
    **/
    2: string orderNo,
    /**
    *  第三方支付流水号
    **/
    3: string thirdPartyPaymentNo,
    /**
    * 支付渠道
    **/
    4: order_enum.TradeTypeEnum tradeType,
    /**
    * 支付金额
    **/
    5: double tradeAmount,
    /**
    * 支付时间
    **/
    6: i64 payTime,
    /**
    * 支付状态
    **/
    7: order_enum.PayStatusEnum payStatus,
    /**
    * 支付类型,1:支付(PAY);2:退款(REFUND);
    **/
    8: order_enum.PaymentTypeEnum paymentType,
    /**
    * 备注
    **/
    9: optional string remark,
    /**
    *创建人
    **/
    10: i32 createdBy,
    /**
    *创建时间
    **/
    11: i64 createdAt,
    /**
    * 修改人
    **/
    12: i32 updatedBy,
    /**
    * 修改时间
    **/
    13: i64 updatedAt,
     /**
        * 关联的支付Id
        **/
    14: i64 originId,

}