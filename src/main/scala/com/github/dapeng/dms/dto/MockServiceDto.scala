package com.github.dapeng.dms.dto

import java.sql.Timestamp

import wangzx.scala_commons.sql.ResultSetMapper

/**
  *
  * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
  * @since 2018-11-09 11:18 AM
  */
case class MockServiceDto(
                           id: Long,

                           service: String,

                           metadataId: Long,

                           createdAt: Timestamp,

                           updatedAt: Timestamp

                         )

object MockServiceDto {
  implicit val resultSetMapper: ResultSetMapper[MockServiceDto] = ResultSetMapper.material[MockServiceDto]
}

