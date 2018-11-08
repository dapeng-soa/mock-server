namespace java com.today.api.order.service

include 'order_request_new.thrift'
include 'order_response_new.thrift'
include 'order_event_new.thrift'
include 'order_vo_new.thrift'

service OrderAsyncService {
    /**
    * 创建一笔订单支付(如果有多次支付，需多次调用)
    **/
    order_response_new.TOrderPaymentResponseNew createOrderPayment(order_request_new.TCreateOrderPaymentRequestNew request)
    (events="com.today.api.order.events.StoreOrderEndEventNew")
}(group="Order")