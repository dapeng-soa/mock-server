namespace java com.today.api.dailyorder.response

include 'dailyorder_enum.thrift'
include 'order_enum.thrift'
include 'page.thrift'

struct DailyOrderItem{
 /**
   * 口座
   */
 1 :  dailyorder_enum.InventoryAccountEnum InventoryAccount,
   /**
   * POS机Id
   */
 2 :  i64 posId,
   /**
   * 营业额
   * @datatype(name="bigdecimal")
   */
 3 : double count
}

/**
* 日结每日营业额返回
**/
struct TQueryOrderDailySumListResponse{
    /**
    *每日营业额列表
    **/
    1: list<DailyOrderItem> dailyOrderList,
    /**
       * 退款总额
       * @datatype(name="bigdecimal")
       */
    2: double refund,
    /**
       * 总代售
       * @datatype(name="bigdecimal")
       */
    3: double totalSale,
    /**
       * 总代收
       * @datatype(name="bigdecimal")
       */
    4: double totalCollection,
    /**
       * 礼品卡充值
       * @datatype(name="bigdecimal")
       */
    5: double giftCardRecharge,
    /**
       * 礼品卡充值促销
       * @datatype(name="bigdecimal")
       */
    6: double giftCardRechargePromotion,
    /**
       * 移动支付
       * @datatype(name="bigdecimal")
       */
    7: double mobilePayment,
    /**
       * 刷卡支付
       * @datatype(name="bigdecimal")
       */
    8: double creditCardPayment,
    /**
       * 会员支付
       * @datatype(name="bigdecimal")
       */
    9: double memberPayment,
    /**
       * 外卖
       * @datatype(name="bigdecimal")
       */
    10: double takeoutFood,
    /**
       * 城市一卡通
       * @datatype(name="bigdecimal")
       */
    11: double cityCard,
    /**
       * 备用金
       * @datatype(name="bigdecimal")
       */
    12: double pettyCash,
    /**
       * 银行送金金额
       * @datatype(name="bigdecimal")
       */
    13: double bankMoney,
    /**
       * 报废金额
       * @datatype(name="bigdecimal")
       */
    14: double scrapAmount,
    /**
    * 现金抹零总金额
    * @datatype(name="bigdecimal")
    **/
    15: double smallCashAmount,
    /**
    * 东合支付
    * @datatype(name="bigdecimal")
    **/
    16: double donghePayment,
    /**
    * 有赞支付
    * @datatype(name="bigdecimal")
    **/
    17: double youzanPayment
}

/**
* 支付详情
**/
struct PaymentDetail{
    /**
    * 支付时间
    **/
    1: optional i64 payTime,
    /**
    * 订单号
    **/
    2: string orderNo,
    /**
    * 支付金额
    * @datatype(name="bigdecimal")
    **/
    3: double tradeAmount,
    /**
    * POS机号
    **/
    4: i64 posId,
    /**
    * 工号
    **/
    5: i32 cashierId,
    /**
    * 支付渠道
    **/
    6: dailyorder_enum.PayTypeEnum tradeType,
    /**
    * 支付总额
    * @datatype(name="bigdecimal")
    **/
    7: double totalAmount,
    /**
    * 支付列表总条数
    **/
    8: i32 totalNum,
    /**
    * 订单溢收金额
    * @datatype(name="bigdecimal")
    **/
    9: double moreEarn
}

/**
* 日结支付详情列表返回
**/
struct TQueryPaymentDetailListResponse{
    /**
    * 日结支付详情列表
    **/
    1: list<PaymentDetail> paymentDetailList
}

/**
* 促销详情
**/
struct PromotionDetail{
    /**
    * 优惠劵id
    **/
    1: i64 couponId,
    /**
    * 优惠劵类型
    **/
    2: string couponType,
    /**
    * 订单号
    **/
    3: string orderNo,
    /**
    * 订单总金额
    * @datatype(name="bigdecimal")
    **/
    4: double orderTotalAmount,
    /**
    * 订单实际付款金额
    * @datatype(name="bigdecimal")
    **/
    5: double orderActualAmount,
    /**
    * 优惠金额
    * @datatype(name="bigdecimal")
    **/
    6: double discountPrice,
    /**
    * 订单完成时间
    **/
    7: i64 finishTime,
    /**
    * POS机编号
    **/
    8: i64 posId,
    /**
    * 工号
    **/
    9: i64 cashierId,
    /**
    * 优惠券key
    **/
    10: string couponKey
}

/**
* 商品促销详情
**/
struct SkuPromotionDetail{
/**
    * 商品促销id
    **/
    1: i64 skuPromotionId,
    /**
    * 商品促销类型
    **/
    2: string skuPromotionType,
    /**
    * 订单号
    **/
    3: string orderNo,
    /**
    * 订单总金额
    * @datatype(name="bigdecimal")
    **/
    4: double orderTotalAmount,
    /**
    * 订单实际付款金额
    * @datatype(name="bigdecimal")
    **/
    5: double orderActualAmount,
    /**
    * 优惠金额
    * @datatype(name="bigdecimal")
    **/
    6: double discountPrice,
    /**
    * 订单完成时间
    **/
    7: i64 finishTime,
    /**
    * POS机编号
    **/
    8: i64 posId,
    /**
    * 工号
    **/
    9: i64 cashierId
}

/**
* 促销详情列表返回
**/
struct TQueryPromotionDetailListResponse{
    /**
    * 优惠详情列表
    **/
    1: list<PromotionDetail> promotionDetailList

    /**
    * 商品优惠详情列表
    **/
    2: list<SkuPromotionDetail> skuPromotionDetailList
}

/**
* 日结列表详情
**/
struct DailyReportListDetail{
    /**
    * 日结编号
    **/
    1: string dailyReportNo,
    /**
    * 营业日期
    **/
    2: i64 serviceDate,
    /**
    * 营业额
    * @datatype(name="bigdecimal")
    **/
    3: double totalAmount,
    /**
    * 退款金额
    * @datatype(name="bigdecimal")
    **/
    4: double refundAmount,
    /**
    * 现金金额
    * @datatype(name="bigdecimal")
    **/
    5: double cashAmount,
    /**
    * 银行送金金额
    * @datatype(name="bigdecimal")
    **/
    6: double bankMoney
}

/**
* 报废详情
**/
struct ScrapDetail{
    /**
    * 中类
    **/
    1:string lev2Category,
    /**
    * 小类
    **/
    2:string lev3Category,
    /**
    * 货号
    **/
    3:string skuNo,
    /**
    * 商品名称
    **/
    4:string skuName,
    /**
    * 规格
    **/
    5:string spec,
    /**
    * 售价
    * @datatype(name="bigdecimal")
    **/
    6:double sellingPrice,
    /**
    * 报废数量
    **/
    7:i32 ScrapAmount,
    /**
    * 报废金额
    * @datatype(name="bigdecimal")
    **/
    8:double ScrapCount,
    /**
    * 类目Id
    **/
    9:i64 categoryId,
}


/**
* 日结报废详情返回
**/
struct ScrapDetailListResponse{
    /**
    * 报废详情列表
    **/
    1:list<ScrapDetail> ScrapDetailList
}

/**
* 日结列表返回
**/
struct ListDailyReportResponse{
    1: list<DailyReportListDetail> DailyReportList,

    2: page.TPageResponse pageResponse
}

struct ZOrderDetail{
    /**
    * pos机编号
    **/
    1:i64 posId,

    /**
    * 开始时间
    **/
    2:string startTime,

    /**
    * 结束时间
    **/
    3:string endTime,

    /**
    * 来客数
    **/
    4:i32 guestCount,

    /**
    * 营业额
    * @datatype(name="bigdecimal")
    **/
    5:double totalAmount,

    /**
    * 移动支付金额
    * @datatype(name="bigdecimal")
    **/
    6:double mobilePayAmount,

    /**
    * 银行卡支付金额
    * @datatype(name="bigdecimal")
    **/
    7:double cardPayAmount,

    /**
    * 外卖金额
    * @datatype(name="bigdecimal")
    **/
    8:double takeOutFoodPayAmount,

    /**
    * 会员支付金额
    * @datatype(name="bigdecimal")
    **/
    9:double memberPayAmount,

    /**
    * 城市一卡通支付金额
    * @datatype(name="bigdecimal")
    **/
    10:double cityCardPayAmount,

    /**
    * 现金支付金额
    * @datatype(name="bigdecimal")
    **/
    11:double cashPayAmount,

    /**
    * 退款金额
    * @datatype(name="bigdecimal")
    **/
    12:double refundAmount,

    /**
    * 代收金额
    * @datatype(name="bigdecimal")
    **/
    13:double collectionAmount,

    /**
    * 代售金额
    * @datatype(name="bigdecimal")
    **/
    14:double saleAmount,

    /**
    * 抹零金额
    * @datatype(name="bigdecimal")
    **/
    15:double smallCashAmount,

    /**
    * 缴金金额
    * @datatype(name="bigdecimal")
    **/
    16:double bankMoneyAmount,
}

/**
* Z账返回体
**/
struct ListZOrderResponse{
    /**
    * Z账详情列表
    **/
    1:list<ZOrderDetail> zOrderDetailList,
}

