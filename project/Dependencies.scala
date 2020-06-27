import sbt._

object Dependencies {
  val sparkVersion = "3.0.0"
  val kafkaVersion = "2.4.1"
  val akkaVersion = "2.5.26"

  val sparkCore =               "org.apache.spark" %% "spark-core" % sparkVersion
  val sparkStreaming =          "org.apache.spark" %% "spark-streaming" % sparkVersion
  val sparkStreamingKafka =     "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion
  val sparkSql =                "org.apache.spark" %% "spark-sql" % sparkVersion
  val kafka =                   "org.apache.kafka" %% "kafka" % kafkaVersion
  val kafkaClients =            "org.apache.kafka" % "kafka-clients" % kafkaVersion
  val akkaHttp =                "com.typesafe.akka" %% "akka-http" % "10.1.12"
  val lift =                    "net.liftweb" %% "lift-json" % "3.0.1"
  val twitterStream =           "org.twitter4j" % "twitter4j-stream" % "4.0.4"
  val sparkCassandraConnector = "com.datastax.spark" %% "spark-cassandra-connector" % "2.5.0"
  val cassandraDriver =         "com.datastax.cassandra" % "cassandra-driver-core" % "3.1.0"
  val logback =                 "ch.qos.logback" % "logback-classic" % "1.1.7"
  val akkaHttpJson =            "de.heikoseeberger" %% "akka-http-json4s" % "1.33.0"
  val akkaActor =               "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val akkaStream =              "com.typesafe.akka" %% "akka-stream" % akkaVersion
  val json4s =                  "org.json4s" %% "json4s-native" % "3.4.2"
  val jansi =                   "org.fusesource.jansi" % "jansi" % "1.12"
  val jodaTime =                "joda-time" % "joda-time" % "2.10.6"
  val googleGuava =             "com.google.guava" % "guava" % "19.0"
}
