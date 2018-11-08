namespace java com.today.api.order.enums
/**
* 订单状态
**/
enum OrderStatusEnum {
    /**
    * 未付款
    **/
    NON_PAYMENT = 1,
    /**
    * 部分付款
    **/
    PART_PAID = 2,
    /**
    * 已完成
    **/
    DONE = 3,
    /**
    * 取消交易
    **/
    CANCELED = 4
}
/**
* 优惠券类型
**/
enum CouponTypeEnum {
    /**
    * 兑换券
    **/
    COUPON = 2,
    /**
    * 满减券
    **/
    VOUCHER = 3,
    /**
    * 现金券
    **/
    CASH_COUPON = 4,
    /**
    * 折扣券
    **/
    DISCOUNT_COUPON = 6,
}
/**
* 支付渠道类型
**/
enum TradeTypeEnum {
    /**
    * 支付宝
    **/
    ALI_PAY = 1,
    /**
    * 微信
    **/
    WECHAT_PAY = 2,
    /**
    * 银行卡
    **/
    BANK_CARD = 3,
    /**
    * 账户余额
    **/
    ACCOUNT = 4,
    /**
    * 优惠券
    **/
    COUPON = 5,
    /**
    * 现金
    **/
    CASH = 6,
    /**
     * 移动支付
    **/
    MOBILE_PAY = 7,
    /**
    * 其他
    **/
    OTHER = 8,
    /**
    *京东到家
    **/
    JINGDONGDAOJIA = 9,
    /**
    * 饿了么外卖
    **/
    ELEME=10,
    /**
    *美团外卖
    **/
    MEITUAN=11,
    /**
    * 城市一卡通
    **/
    ONECARD=12,
    /**
    * 百度钱包
    **/
    BAIDU_WALLET=13,
    /**
    *  京东钱包
    **/
    JINGDONG_WALLET=14,
    /**
    *  百度糯米
    **/
    BAIDU_NUOMI = 15,
    /**
    * QQ钱包
    **/
    QQ_WALLET = 16,
    /**
    *  翼支付
    **/
    YI_PAY = 17,
    /**
    * 银联二维码
    **/
    ICBC_PAY=18,
    /**
    * 东合支付
    **/
    DONGHE_PAY=19
    /**
    * 有赞支付
    **/
    YOUZAN_PAY=20
    /**
    * 银联二维码
    **/
    UNION_QR_CODE=512
}
/**
* 非码支付类型
**/
enum FeiMaPayEnum{
    /**
    * 支付宝
    **/
    ALI_PAY = 10001,
    /**
     * 微信
    **/
    WECHAT_PAY = 10004,
    /**
    * 百度钱包
    **/
    BAIDU_WALLET = 10008,
    /**
     * 银联
    **/
    BANK_CARD = 10031,
    /**
    * 京东钱包
    **/
    JINGDONG_WALLET = 10017,
    /**
    * 百度糯米
    **/
    BAIDU_NUOMI = 10026,
    /**
    * QQ钱包
    **/
    QQ_WALLET = 10015,
    /**
      * 翼支付
     **/
    YI_PAY = 10010,
     /**
       * 工银二维码
      **/
    UNION_MOBILE_PAY = 10025,

}
/**
* 支付流水状态
*
* 1.未支付,2.已支付,3退款中(废弃),4.已退款,5.超时,6.失败.
**/
enum PayStatusEnum {
    /**
    * 未支付
    **/
    NON_PAYMENT = 1,
    /**
    * 已支付
    **/
    PAID = 2,
    /**
    * 退款中 (废弃)
    **/
    REFUNDING = 3,
    /**
    * 已退款
    **/
    REFUNDED = 4,
    /**
    * 超时
    **/
    TIMEOUT = 5,
    /**
    * 失败
    **/
    FAILED = 6,
    /**
    * 已冲正
    **/
    REVERSED = 7
}
/**
* 支付流水类型
* 1.支付 2.退款
**/
enum PaymentTypeEnum {
    /**
    * 支付
    **/
    PAY = 1,
    /**
    * 退款
    **/
    REFUND = 2
}
/**
* 订单详情(SKU)状态
**/
enum OrderDetailSkuStatusEnum {
    /**
    * 正常
    **/
    NOMAL = 0,
    /**
    * 已删除
    **/
    CANCEL = 1
}

/**
* 100成功 201支付账号不存在 202账户异常 203账户余额不足 其他:超时或者系统错误
**/
enum TradeResultCode {
/**
* 正常
**/
    SUCCESS = 100,
    /**
    * 账户不存在
**/
    ACCOUNT_NOT_EXISTS = 101,
    /**
    * 账户异常
**/
    ACCOUNT_ERROR = 102,
    /**
    * 账户余额不足
**/
    INSUFFICIENT_BALANCE = 103,
    /**
    * 交易超时
**/
    TIME_OUT = 104,
    /**
    * 系统错误
**/
    SYSTEM_ERROR = 105,
    /**
    * 支付码错误
    **/
    PAY_CODE_ERROR = 106,

/**
* 会员不存在
**/
    MEMBER_NOT_EXIST = 201,
/**
* 微信卡包中不存在会员卡
**/
    MEMBER_WECHAT_CARD_NOT_EXIST =202,
/**
* 重复请求
**/
    MEMBER_REPEAT_REQUEST = 203,
/**
* 用户余额不足
**/
    MEMBER_BALANCE_NOT_ENOUGH = 204,
/**
* 会员数据异常
**/
    MEMBER_DATA_ERROR = 205,
/**
* 退款金额超过订单金额
**/
    MEMBER_REFUND_AMOUNT_ERROR = 206
/**
* 会员优惠劵不存在
**/
    MEMBER_COUPON_NOT_EXIST = 207
/**
* 优惠劵已核销
**/
    MEMBER_COUPON_USED = 208
/**
* 优惠劵已过期，不能重复使用
**/
    MEMBER_COUPON_EXPIRE = 209
/**
* 调用会员出现未知异常
**/
    MEMBER_UNKONW_EX = 210

/**
* 会员账户余额为0或没有可用的礼品卡
**/
    MEMBER_NO_GIFT_CARD = 211

}

enum FeiMaReqType {
    /**
    *  支付
    **/
    PAYMENT = 72,
    /**
    * 退款
    **/
    REFUND = 62,
    /**
    * 冲正
    **/
    CORRECT_PAYMENT = 3
}

enum RefundStyleEnum {
    /**
    *  订单完成后退款
    **/
    ORDER_DONE_REFUND = 0,
    /**
    * 订单支付中退款
    **/
    ORDER_UN_DONE_REFUND = 1
}

enum SpotTypeEnum {
   /**
    *  抹零支付
    **/
    PAYMENT_SPOT = 1,
    /**
    * 退款垫付
    **/
    REFUND_SPOT = 2
}
/**
* 订单类型
**/
enum OrderTypeEnum {
    /**
    * 门店pos端-订单
    **/
    POS_ORDER = 1,
    /**
    * mini货架端-订单，
    **/
    MINI_ORDER = 2,
    /**
    * 预定自提订单
    **/
    PREDICT_SELF_ORDER = 3,
    /**
    * 扫码购物-订单
    **/
    SCAN_PURCHASE_ORDER = 4,
    /**
    * 样品-订单
    **/
    SAMPLE_ORDER = 5,
    /**
    * 礼品卡充值-订单
    **/
    CARD_RECHARGE_ORDER=6

}
/**
* 退款类型
**/
enum OrderRefundEnum {
    /**
    * 未退款
    **/
    NON_REFUND = 0,
    /**
    * 部分退款
    **/
    PART_REFUND = 1,
    /**
    * 全部退款
    **/
    REFUNDED = 2,
}

/**
  *  支付渠道
**/
enum OrderChannelEnum{
    /**
    *  支付渠道
    **/
    FEIMA_PAY = 0,
    /**
    * 退款垫付
    **/
    YIMA_PAY = 1



}