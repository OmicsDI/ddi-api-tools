package org.omics.sparkop

import org.apache.hadoop.fs.LocalFileSystem
import org.apache.hadoop.hdfs.DistributedFileSystem
import org.apache.spark.sql.{DataFrame, SQLContext, SparkSession}
import org.omics.utils.Constants

object SparkInfo {

  def getSparkSession():SparkSession = {
    val sparkSession = SparkSession.builder()
      .master(Constants.sparkMasterUrl)
      .appName(Constants.sparkAppName)
      .config(Constants.mongoInputUriKey, Constants.prodMongoUri)
      .config(Constants.mongOutUriKey, Constants.prodMongoUri)
      .getOrCreate()
    sparkSession.conf.set("spark.driver.memory","6g")
    sparkSession.conf.set("spark.executor.memory", "5g")
    sparkSession.conf.set("fs.hdfs.impl", classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName)
    sparkSession.conf.set("fs.file.impl", classOf[org.apache.hadoop.fs.LocalFileSystem].getName)
    sparkSession
  }

  def getSqlContext():SQLContext = {
    getSparkSession().sqlContext
  }

  def readCsv(filePath:String,sc:SQLContext): DataFrame =
  {
    val csvData = sc.read.format("csv")
      .option("header", "true")//.schema(schema)
      .option("inferSchema",true)
      .option("delimiter", "\t")
      .load(filePath)

    csvData
  }

}
