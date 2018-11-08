namespace java com.today.api.dailyorder.enums



/**
* 口座枚举类型
**/
enum InventoryAccountEnum {
   /**
   * 快速饮料
   **/
   QUICK_DRINK = 1,
   /**
   * 茶叶蛋
   **/
   TEA_EGGS = 2,
   /**
   * 香烟
   **/
   CIGARETTE = 3,
   /**
   * 酒
   **/
   WINE = 4,
   /**
   * 应税16%
   **/
   TAXABLE_SIXTEEN_PERCENT = 5,
   /**
   * 应税10%
   **/
   TAXABLE_TEN_PERCENT = 6,
   /**
   * 应税3%
   **/
   TAXABLE_THREE_PERCENT = 7,
   /**
   * 应税0%
   **/
   TAXABLE_ZERO_PERCENT = 8,
}

/**
* （日结查询）支付类型枚举
**/
enum PayTypeEnum {
    /**
    * 会员支付
    **/
    MEMBER_PAY = 1,
    /**
    * 刷卡支付
    **/
    CARD_PAY = 2,
    /**
    * 城市一卡通支付
    **/
    CITYCARD_PAY = 3,
    /**
    * 外卖
    **/
    TAKEOUTFOOD_PAY = 4,
    /**
    * 现金支付
    **/
    CASH_PAY = 5,
    /**
    * 移动支付
    **/
    MOBILE_PAY = 6,
    /**
    * 东合支付
    **/
    DONGHE_PAY = 7,
    /**
    * 有赞支付
    **/
    YOUZAN_PAY = 8,
}