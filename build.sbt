import Dependencies._

name := """Lambda-Arch-Spark"""

lazy val commonSettings = Seq(
  organization := "com.knoldus",
  version := "0.1.0",
  scalaVersion := "2.12.6"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "Lambda-Arch-Spark",
    libraryDependencies ++= Seq(
      sparkCore,
      sparkStreaming,
      sparkSql,
      sparkStreamingKafka,
      kafkaClients,
      kafka,
      akkaActor,
      akkaHttp,
      akkaStream,
      lift,
      twitterStream,
      sparkCassandraConnector,
      cassandraDriver,
      logback,
      json4s,
      jansi,
      jodaTime,
      akkaHttpJson,
      googleGuava
    )
  )