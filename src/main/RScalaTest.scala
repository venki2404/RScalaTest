package main

import _root_.jobs.drillDown.DrillDownArtist
import _root_.spark.jobserver.SparkHiveJob
import _root_.spark.jobserver.SparkJobValid
import _root_.spark.jobserver.SparkJobValidation
import breeze.linalg.DenseVector
import com.typesafe.config.Config
import com.typesafe.config.Config
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.{ DataFrame, Row }
import org.ddahl.rscala.callback.RClient
import spark.jobserver.{ SparkJobInvalid, SparkJobValid, SparkJobValidation }

import scala.util.Try

/**
 * Connect to mongo DB and provide a drill down for the artist
 */
object RScalaTest extends SparkHiveJob {

  def validate(sql: HiveContext, config: Config): SparkJobValidation = SparkJobValid

  def runJob(sql: HiveContext, config: Config): Any = {
    val conf = new SparkConf().setAppName("pipelineTest").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val sql = new SQLContext(sc)

    val R = RClient()
  }
}
