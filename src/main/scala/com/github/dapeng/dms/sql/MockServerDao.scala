package com.github.dapeng.dms.sql

import com.github.dapeng.dms.dto.MockServiceDto
import com.github.dapeng.dms.dto.request.ListServiceRequest
import javax.sql.DataSource
import org.slf4j.LoggerFactory
import wangzx.scala_commons.sql._
import scala.collection.JavaConverters._

/**
  *
  * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
  * @since 2018-11-09 10:45 AM
  */
object MockServerDao {
  private val log = LoggerFactory.getLogger(getClass)

  import com.github.dapeng.dms.util.Implicits._

  val dataSource: DataSource = MockDataSource.dataSource

  /**
    * listServicesByCondition
    */
  def listServicesByCondition(request: ListServiceRequest): java.util.List[MockServiceDto] = {
    val querySql =sql"""SELECT * FROM mock_service where 1=1 """

    val optionSql = List[SQLWithArgs](
      request.serviceId.optional(id => sql"""AND id = ${id}"""),
      request.serviceName.optional(fullName => sql" AND service = ${fullName} "),
      request.simpleName.optional(simpleName => sql" AND service like ${simpleName} ")
    ).reduceLeft(_ + _)

    val limitSql = request.pageRequest.optional(page => sql" LIMIT ${page.start} , ${page.limit}")
    val sortSql = request.pageRequest.optional(page ⇒ page.sortFields.optional(sort ⇒ sql"ORDER BY sort DESC "))
    //execute
    val executeSql = querySql + optionSql + limitSql + sortSql
    log.info("listServicesByCondition: executeSql sql: {}", executeSql)
    val serviceList: List[MockServiceDto] = dataSource.rows[MockServiceDto](executeSql)

    serviceList.asJava
  }

}


