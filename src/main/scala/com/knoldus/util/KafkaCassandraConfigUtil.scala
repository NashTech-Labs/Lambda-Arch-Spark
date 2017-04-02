package com.knoldus.util

import scala.collection.JavaConversions._

import com.typesafe.config.ConfigFactory


object KafkaCassandraConfigUtil {

  val config = ConfigFactory.load()

  val consumerKey = config.getString("app.consumerKey")
  val consumerSecret = config.getString("app.consumerSecret")
  val accessToken = config.getString("app.accessToken")
  val accessTokenSecret = config.getString("app.accessTokenSecret")
  val port = config.getInt("cassandra.port")
  val hosts = config.getStringList("cassandra.hosts").toList
  val cassandraKeyspaces = config.getStringList("cassandra.keyspaces")
  val replicationFactor = config.getString("cassandra.replication_factor").toInt
  val readConsistency = config.getString("cassandra.read_consistency")
  val writeConsistency = config.getString("cassandra.write_consistency")

}
