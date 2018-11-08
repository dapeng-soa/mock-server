namespace java com.today.api.dailyorder.request
include 'order_enum.thrift'
include 'page.thrift'
include 'dailyorder_enum.thrift'

/**
* 日结每日营业额请求
**/
struct TQueryOrderDailySumListRequest{
    /**
    * 门店号
    **/
    1: string storeId,
    /**
    * 营业时间
    **/
    2: i64 businessTime,
}

/**
* 日结支付详情列表请求
**/
struct TQueryPaymentDetailListRequest{
    /**
     *分页
    **/
    1: page.TPageRequest pageRequest,
    /**
    * 门店号
    **/
    2: string storeId,
    /**
    * 支付方式
    **/
    3: dailyorder_enum.PayTypeEnum tradeType
    /**
    * 营业时间
    **/
    4: i64 businessTime,
}

/**
* 促销详情列表请求
**/
struct TQueryPromotionDetailListRequest{
    /**
    * 门店id
    **/
    1: string storeId,
    /**
    * 营业时间
    **/
    2: i64 businessTime,
}

struct TQueryScrapDetailListRequest{
    /**
    * 门店id
    **/
    1: string storeId,
    /**
    * 营业时间
    **/
    2: i64 businessTime,
}

/**
* 日结列表请求
**/
struct ListDailyReportListRequest{
    /**
     *分页
    **/
    1: page.TPageRequest pageRequest,
    /**
    * 门店号
    **/
    2: string storeId,
    /**
    * 日结编号
    **/
    3: optional string dailyReportNo,

    /**
    * 营业日期开始
    **/
    4: optional i64 startTime,

    /**
    * 营业日期结束
    **/
    5: optional i64 endTime,
}

/**
* Z账请求
**/
struct TQueryZOrderRequest{
    /**
    * 门店id
    **/
    1: string storeId,
    /**
    * 营业时间
    **/
    2: i64 businessTime,
}
