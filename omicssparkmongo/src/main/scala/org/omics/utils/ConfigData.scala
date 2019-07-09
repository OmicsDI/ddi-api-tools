package org.omics.utils
import com.typesafe.config.{Config, ConfigFactory}

object ConfigData {

    def getConfig(filePath:String="/home/gaur/omics/ddi-api-tools/omicssparkmongo/src/main/resources/application.conf"):Config = {
      val conf = ConfigFactory.load(filePath)
      conf
    }

}
