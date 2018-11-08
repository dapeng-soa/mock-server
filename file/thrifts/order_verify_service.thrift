namespace java com.today.api.verifyorder.service
include 'order_verify_request.thrift'
/**
* order验证服务
**/
service OrderVerifyService {

/**
* 订单数据验证，pos通过此接口传送一天的订单数据
**/
 i32 verifyOrders(order_verify_request.VerifyWholeOrdersRequest request)

 /**
 * 日志上报接口
 **/
  void uploadPosLogs(order_verify_request.TPosLogRequest request)

  /**
   * 日志上报接口 批量
   **/
    void batchUploadPosLogs(list<order_verify_request.TPosLogRequest> request)
}(group="Order")