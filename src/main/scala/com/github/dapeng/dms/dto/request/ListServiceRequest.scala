package com.github.dapeng.dms.dto.request

/**
  *
  * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
  * @since 2018-11-09 10:54 AM
  */
case class ListServiceRequest(simpleName: Option[String],

                              serviceName: Option[String],

                              serviceId: Option[Long],

                              pageRequest: Option[PageRequest])


object Convert {

  import com.github.dapeng.dms.util.Implicits._

  def convertNull(request: ListServiceRequest): ListServiceRequest = {
    ListServiceRequest(request.simpleName.nullable,
      request.simpleName.nullable,
      request.serviceId.nullable,
      request.pageRequest.nullable)
  }

}
