package myPackage

import breeze.linalg.DenseVector
import breeze.stats.distributions.{ Poisson, Uniform }
import com.typesafe.config.Config
import org.apache.spark.sql.SQLContext
import org.apache.spark.{ SparkConf, SparkContext }
import org.ddahl.rscala.callback.RClient
import spark.jobserver.{ SparkJobValid, SparkJobValidation, SparkSqlJob }

case class Student(name: String, age: Int)
object RScalaTest extends SparkSqlJob {

  def validate(sql: SQLContext, config: Config): SparkJobValidation = SparkJobValid

  def runJob(sql: SQLContext, config: Config): Any = {
    executePayload(sql)
  }

  def executePayload(sql: SQLContext): Any = {
    import sql.implicits._

    val studentsDataSet = Seq(Student("Torcuato", 27), Student("Rosalinda", 34)).toDS()
    studentsDataSet.collect

    val R = RClient()
    val x = Uniform(50, 60).sample(1000)
    val eta = x map { xi => (xi * 0.1) - 3 }
    val mu = eta map {
      math.exp(_)
    }
    val y = mu map {
      Poisson(_).draw
    }
    R.x = x.toArray // send x to R
    R.y = y.toArray // send y to R
    R.eval("mod <- glm(y~x,family=poisson())") // fit the model in R
    R.eval("library(robfilter)")
    R.eval("wrms <- wrm.smooth(c(1:5), c(3,4,7,1,2), h=3)")
    val wrms = DenseVector[Double](R.evalD1("wrms$y"))
    println(wrms)

    return "some value"
  }

  def main(args: Array[String]) = {
    val conf = new SparkConf().setAppName("localTest").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val sql = new SQLContext(sc)

    executePayload(sql)
    sc.stop
  }
}
