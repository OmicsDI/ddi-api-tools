package org.omics.utils

object Constants {

  val config = ConfigData.getConfig("")

  val accession = "accession"
  val datasetDatabase = "database"
  val scoreSearchCount = "scores.searchCount"
  val additionalSearchCount = "additional.search_count"

  val prodMongoUri = config.getString("mongodb.produri")
  val devMongoUri = config.getString("mongodb.devuri")


  val sparkMasterUrl = config.getString("spark.masterurl")
  val sparkAppName = config.getString("spark.appName")
  val mongOutUriKey = config.getString("spark.mongouturikey")
  val mongoInputUriKey = config.getString("spark.mongoinputurikey")



}
