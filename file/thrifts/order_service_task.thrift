namespace java com.today.api.order.service


service OrderScheduledService{



 /**
 *定时跑P,处理超时的订单
 **/
 void loseOrder()

 /**
 * 订单冲正
    **/
 void reverseOrderByOrderNo(1: string orderNo)
}(group="Order")

