package org.omics.sparkop

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.omics.utils.Constants

object SparkInfo {

  def getSparkSession():SparkSession = {
    val sparkSession = SparkSession.builder()
      .master(Constants.sparkMasterUrl)
      .appName(Constants.sparkAppName)
      .config(Constants.mongoInputUriKey, Constants.devMongoUri)
      .config(Constants.mongOutUriKey, Constants.devMongoUri)
      .getOrCreate()
    sparkSession
  }

  def getSqlContext():SQLContext = {
    getSparkSession().sqlContext
  }



}
