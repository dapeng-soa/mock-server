package com.github.dapeng.dms.sql

import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Autowired

/**
  *
  * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
  * @since 2018-11-09 10:46 AM
  */

object MockDataSource {

  @Autowired
  var dataSource: DataSource = _

  def getInstance(): MockDataSource.this.type = this

}
