namespace java com.today.api.verifyorder.request
include 'order_enum.thrift'





/**
* 验证订单数据请求体
**/
struct TOrderVerify {
    /**
    * 订单编号
    **/
    1: string orderNo,
    /**
    * POS机编号
    **/
     2:  i32 posId,
    /**
    * 支付方式
    **/
    3:string payType,
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
    * 订单优惠/折扣金额
    * @datatype(name="bigdecimal")
    **/
    7:double discountAmount,

    /**
    * 订单优惠/折扣金额
    * @datatype(name="bigdecimal")
    **/
    8:double refundAmount,
    /**
    *订单创建时间
    **/
    9:i64 createTime,
    /**
    *订单支付时间
    **/
    10:optional i64 payTime,
    /**
    * 收银员
    **/
    11: i64 cashierId,
    /**
    * 门店编号
    **/
    12: string storeId,
    /**
    * 会员id
    **/
    13: optional i64 memberId,
    /**
    * 用户手机号
    **/
    14: optional string mobile,
    /**
    * 订单备注
    **/
    15: optional string remark,
    /**
    * 订单状态,1:未付款(NON_PAYMENT);2:部分付款(PART_PAID);3:已完成(DONE);4:取消交易(CANCELED);
    **/
    16: i32 orderStatus,
    /**
    * 订单 退款状态，0:未退款(NON_REFUND),1:部分退款(PART_REFUND);2:已退款(REFUNDED)
    **/
    17:i32 refundStatus
}


/**
* 验证订单详情请求体
**/
struct TOrderDetailVerify {
     /**
        * 订单编号
        **/
        1: string orderNo,
        /**
        * 订单详情行号, 针对同一Sku存在多行的情况设计
        **/
        2: i32 detailSeq,
        /**
         * 商品skuNo
        **/
        3: string skuNo,

        /**
         * 商品名称
        **/
        4: optional string skuName,
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
        10: double discountAmount,
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
        * 订单相求状态，正常0，已退款为1
        **/
        14:i32 skuStatus
}

/**
* 订单验证流水表
**/
struct TOrderPaymentVerify {

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
    * 第三方流水号
    **/
    12:optional string thirdPartyPaymentNo


}

/**
* 订单验证会员券表
**/
struct TOrderCouponVerify {
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
        * 商品编号
        **/
         5:optional string skuNo,
        /**
        * 优惠金额
        *  @datatype(name="bigdecimal")
        **/
         6:double discountPrice

}

/**
* 一次性所有请求
**/
struct VerifyWholeOrdersRequest {
       /**
       * 订单验证 订单list
        **/
        1:list<TOrderVerify> orders,
        /**
        * 订单验证 订单详情list
        **/
        2:list<TOrderDetailVerify> orderDetails,
        /**
        *订单验证流水list
        **/
        3:list<TOrderPaymentVerify> orderPayments,
        /**
        * 订单验证会员券list
        **/
       4:list<TOrderCouponVerify> orderCoupons
}


/**
* 一次性所有请求
**/
struct TPosLogRequest {
        /**
         * 门店编号
        **/
        1: string storeId,
        /**
        * POS机编号
        **/
        2:  i32 posId,
        /**
        * 日志时间
        **/
        3:i64 logTime,
        /**
        *日志类型
        **/
        4:i32 logType,
        /**
        * 日志内容
        **/
        5:string logInfo,
        /**
        * 版本号
        **/
        6:string version
}
