name := "RScalaTest"
organization := "main.RScalaTest"
version := "0.0.1.SNAPSHOT"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation")
lazy val spark = "1.6.1"

resolvers += "Job Server Bintray" at "https://dl.bintray.com/spark-jobserver/maven"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % spark % "provided",
  "org.apache.spark" %% "spark-hive" % spark % "provided",
  "org.apache.spark" %% "spark-sql" % spark % "provided",
  "org.apache.spark" %% "spark-mllib" % spark % "provided",
  "spark.jobserver" %% "job-server-api" % "0.6.1" % "provided",
  "spark.jobserver" %% "job-server-extras" % "0.6.1" % "provided"
)

unmanagedJars in Compile += file("externalLib/rscala_2.11-1.0.9.jar")

assemblyMergeStrategy in assembly := {
  case PathList(ps@_*) if ps.last endsWith "Console.class" => MergeStrategy.first
  case PathList(ps@_*) if ps.last endsWith "Console$.class" => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

mainClass := Some("jobs.ScalaToRTest")
