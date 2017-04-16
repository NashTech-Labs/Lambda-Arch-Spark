import Dependencies._

name := """Lambda-Arch-Spark"""


spName := "knoldus/Lambda-Arch-Spark"

sparkVersion := "2.0.0"

sparkComponents ++= Seq("core","streaming", "sql")

licenses += "Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0")

spIncludeMaven := true

credentials += Credentials("Spark Packages Realm",
  "spark-packages.org",
  sys.props.getOrElse("GITHUB_USERNAME", default = ""),
  sys.props.getOrElse("GITHUB_PERSONAL_ACCESS_TOKEN", default = ""))



lazy val commonSettings = Seq(
  organization := "com.knoldus",
  version := "0.1.0",
  scalaVersion := "2.11.8"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "Lambda-Arch-Spark",
    libraryDependencies ++= Seq(kafka, akkaHttp, lift, twitterStream, sparkStreamingKafka, sparkCassandraConnect, cassandraDriver, logback, akkaHttpJson, jansi, json4s)
  )