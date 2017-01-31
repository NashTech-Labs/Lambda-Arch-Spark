name := """Lambda-Arch-Spark"""

version := "1.0"

scalaVersion := "2.11.8"

val akkaHttpVersion = "2.4.7"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
                             "org.apache.spark" %% "spark-core" % "2.0.0",
                            "org.apache.spark" %% "spark-streaming" % "2.0.0",
                            "org.apache.spark" %% "spark-streaming-kafka-0-8" % "2.0.0",
                            "org.apache.kafka" %% "kafka" % "0.8.2.1",
                           "com.typesafe.akka" %% "akka-http-experimental"  % akkaHttpVersion,
                           "net.liftweb" %% "lift-json"  % "2.6",
                            "org.twitter4j" % "twitter4j-stream" % "4.0.4",
                           "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0-M3",
                           "com.datastax.cassandra" % "cassandra-driver-core" % "3.1.0",
                           "ch.qos.logback" %  "logback-classic" % "1.1.7",
                           "org.apache.spark" %% "spark-sql" % "2.0.0",
                           "de.heikoseeberger"          %% "akka-http-json4s"               % "1.7.0",
                           "org.json4s"    %% "json4s-native"      %  "3.3.0",
                            "org.fusesource.jansi"       %  "jansi"                          % "1.12"

                           )
