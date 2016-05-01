name := "RScalaTest"
organization := "main.RScalaTest"
version := "0.0.1.SNAPSHOT"

scalaVersion := "2.10.6"

scalacOptions ++= Seq("-deprecation")

//The default SBT testing java options are too small to support running many of the tests
// due to the need to launch Spark in local mode.
//javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled")
parallelExecution in Test := false


lazy val spark = "1.6.1"

resolvers += "Job Server Bintray" at "https://dl.bintray.com/spark-jobserver/maven"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % spark % "provided",
  "org.apache.spark" %% "spark-hive" % spark % "provided",
  "com.stratio.datasource" %% "spark-mongodb" % "0.11.1",
  "org.apache.spark" %% "spark-sql" % spark % "provided",
  "org.apache.spark" %% "spark-mllib" % spark % "provided",
  "spark.jobserver" %% "job-server-api" % "0.6.1" % "provided",
  "spark.jobserver" %% "job-server-extras" % "0.6.1" % "provided",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  //TODO use correct spark version here
  "com.holdenkarau" %% "spark-testing-base" % "1.6.1_0.3.3" % "test"
)

unmanagedJars in Compile += file("externalLibraries/rscala_2.10-1.0.9.jar")

assemblyMergeStrategy in assembly := {
  case PathList(ps@_*) if ps.last endsWith "Console.class" => MergeStrategy.first
  case PathList(ps@_*) if ps.last endsWith "Console$.class" => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
