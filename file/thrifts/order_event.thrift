namespace java com.today.api.order.events


include 'order_vo.thrift'
include 'order_request.thrift'

/**
* 创建订单, 由于需要消费者做幂等,故加上事件Id
**/
struct CreateOrderEvent {
    /**
    * 事件Id
    **/
    1: i64 id,

    2: order_vo.TOrder order,

    3: list<order_vo.TOrderDetail> orderDetail

}

struct CancelOrderEvent {
    /**
    * 事件Id
    **/
    1: i64 id,

    2: order_vo.TOrder order,

    3: list<order_vo.TOrderDetail> orderDetail

}


struct PartRefundEvent {
    /**
    * 事件Id
    **/
    1: i64 id,

    2: order_vo.TOrder order,

    3: list<order_vo.TOrderDetail> orderDetail

}

struct StoreOrderEndEvent {
    /**
    * 事件Id
    **/
    1: i64 id,
    /**
    * 积分
    **/
    2: i32 score,

    3: order_vo.TOrder order,

    4: list<order_vo.TOrderDetail> orderDetail

}
