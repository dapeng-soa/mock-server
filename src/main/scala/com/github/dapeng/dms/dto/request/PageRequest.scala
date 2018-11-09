package com.github.dapeng.dms.dto.request

/**
  * 分页查询请求包
  *
  * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
  * @since 2018-11-09 11:03 AM
  */
case class PageRequest(

                        /**
                          *
                          * *
                          * 查询的开始序号（序号从零开始）
                          *
                          **/

                        start: Int,

                        /**
                          *
                          * *
                          * 返回记录数
                          *
                          **/

                        limit: Int,

                        /**
                          *
                          * *
                          * 排序的字段
                          *
                          **/

                        sortFields: Option[String] = None
                      )
