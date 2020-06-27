package com.knoldus.util

import java.util

import scala.collection.JavaConversions._
import com.typesafe.config.{Config, ConfigFactory}

object KafkaCassandraConfigUtil {

  val config: Config = ConfigFactory.load()

  val consumerKey: String = config.getString("app.consumerKey")
  val consumerSecret: String = config.getString("app.consumerSecret")
  val accessToken: String = config.getString("app.accessToken")
  val accessTokenSecret: String = config.getString("app.accessTokenSecret")
  val port: Int = config.getInt("cassandra.port")
  val hosts: List[String] = config.getStringList("cassandra.hosts").toList
  val cassandraKeyspaces: util.List[String] = config.getStringList("cassandra.keyspaces")
  val replicationFactor: Int = config.getString("cassandra.replication_factor").toInt
  val readConsistency: String = config.getString("cassandra.read_consistency")
  val writeConsistency: String = config.getString("cassandra.write_consistency")

}
