package com.github.dapeng.dms.thrift

import java.io.{File, FilenameFilter}
import java.util

import com.github.dapeng.code.generator.MetadataGenerator
import com.github.dapeng.code.parser.ThriftCodeParser
import com.github.dapeng.core.metadata.Service

/**
  *
  * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
  * @since 2018-11-07 10:07 PM
  */
object ThriftGenerator {

  def main(args: Array[String]): Unit = {
    generateFiles("/Users/maple/ideaspace/dapeng/dapeng-mock-server/file/thrifts", "/Users/maple/ideaspace/dapeng/dapeng-mock-server/file/resources")
  }


  def generateFiles(sourceFilePath: String, resourceTargetPath: String): (util.List[Service], Array[String]) = {
    println("Welcome to use generate plugin")

    val inDir = sourceFilePath


    val thriftResources: Array[String] = new File(inDir).listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = name.endsWith(".thrift")
    }).map(file => file.getAbsolutePath)

    val parserLanguage = "scala"
    val version = "1.0.0"

    val services = new ThriftCodeParser(parserLanguage).toServices(thriftResources, version)

    new MetadataGenerator().generate(services, resourceTargetPath)

    (services, thriftResources)
  }

}
