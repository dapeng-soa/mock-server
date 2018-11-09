package com.github.dapeng.dms.util

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

  implicit class Nullable[T](opt: Option[T]) {

    import wangzx.scala_commons.sql._

    def nullable(): Option[T] = opt match {
      case Some(value) => Option.apply(value)
      case None => Option.empty[T]
      case null => Option.empty[T]
    }

  }

}
