package com.github.dapeng.dms.sql

import com.github.dapeng.dms.dto.MockServiceDto
import com.github.dapeng.dms.web.vo.request.{ListServiceReq, ListServiceRequest}
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
  def listServicesByCondition(request: ListServiceReq): java.util.List[MockServiceDto] = {
    val querySql =sql"""SELECT * FROM mock_service where 1=1 """
    val optionSql = List[SQLWithArgs](
      request.getServiceId.nullable(id => sql""" AND id = ${id.toLong}"""),
      request.getServiceName.nullable(fullName => sql" AND service = ${fullName} "),
      request.getSimpleName.nullable(simpleName => sql" AND service like concat('%',${simpleName},'%')")
    ).reduceLeft(_ + _)

    val pageRequest = request.getPageRequest

    val limitSql = if (pageRequest != null) {
      request.getPageRequest.nullable(page => sql"limit  ${page.getStart.toInt}, ${page.getLimit.toInt}")
    } else SQLWithArgs("", Seq.empty)

    //    val sortSql = request.pageRequest.optional(page ⇒ page.sortFields.optional(sort ⇒ sql"ORDER BY sort DESC "))
    //execute
    val executeSql = querySql + optionSql + limitSql
    log.info("listServicesByCondition: executeSql sql: {}", executeSql)
    val serviceList: List[MockServiceDto] = dataSource.rows[MockServiceDto](executeSql)

    serviceList.asJava
  }

}


