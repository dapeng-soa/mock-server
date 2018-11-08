namespace java com.today.api.order.events


include 'order_vo_new.thrift'
include 'order_request_new.thrift'

/**
* 订单完成时,发送订单完成事件,库存监听后消库存,
* 创建订单, 由于需要消费者做幂等,故加上事件Id
**/
struct CreateOrderEventNew {
    /**
    * 事件Id
    **/
    1: i64 id,

    2: order_vo_new.TOrderNew order,

    3: list<order_vo_new.TOrderDetailNew> orderDetail

}
/**
* 订单全部退款时，发送此事件，库存监听并回收库存
**/
struct CancelOrderEventNew {
    /**
    * 事件Id
    **/
    1: i64 id,

    2: order_vo_new.TOrderNew order,

    3: list<order_vo_new.TOrderDetailNew> orderDetail

}

/**
* 订单完成后,部分退款
**/
struct PartRefundEventNew {
    /**
    * 事件Id
    **/
    1: i64 id,

    2: order_vo_new.TOrderNew order,

    3: list<order_vo_new.TOrderDetailNew> orderDetail

}
/**
* 订单完成后，发送的事件
**/
struct StoreOrderEndEventNew {
    /**
    * 事件Id
    **/
    1: i64 id,
    /**
    * 积分
    **/
    2: i32 score,

    3: order_vo_new.TOrderNew order,

    4: list<order_vo_new.TOrderDetailNew> orderDetail,
    /**
    * 优惠券信息
    **/
    5: list<string> orderCoupons
}