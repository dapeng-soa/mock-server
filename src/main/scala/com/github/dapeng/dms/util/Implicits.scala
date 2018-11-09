package com.github.dapeng.dms.util

import wangzx.scala_commons.sql.SQLWithArgs

/**
  *
  * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
  * @since 2018-11-09 11:40 AM
  */
object Implicits {

  implicit class SqlOptionEx[T](opt: Option[T]) {

    import wangzx.scala_commons.sql._

    def optional(op: T => SQLWithArgs): SQLWithArgs = opt match {
      case Some(value) => op(value)
      case None => SQLWithArgs("", Seq.empty)
    }

  }

  implicit class Nullable[T](opt: T) {
    def nullable(op: T ⇒ SQLWithArgs): SQLWithArgs = {
      if (opt != null) {
        op(opt)
      } else {
        SQLWithArgs("", Seq.empty)
      }
    }

  }

}
