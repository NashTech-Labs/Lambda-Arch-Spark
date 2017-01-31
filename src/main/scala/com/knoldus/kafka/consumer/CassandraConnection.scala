package com.knoldus.kafka.consumer


import java.util.Date

import com.datastax.driver.core._
import com.knoldus.util.KafkaCassandraConfigUtil._
import org.slf4j.LoggerFactory

trait CassandraConnection {

  val logger = LoggerFactory.getLogger(getClass.getName)
  val defaultConsistencyLevel = ConsistencyLevel.valueOf(writeConsistency)
  val cassandraConn: Session = {
    val cluster = new Cluster.Builder().withClusterName("Test Cluster").
      addContactPoints(hosts.toArray: _*).
      withPort(port).
      withQueryOptions(new QueryOptions().setConsistencyLevel(defaultConsistencyLevel)).build
    val session = cluster.connect
    session.execute(s"CREATE KEYSPACE IF NOT EXISTS  ${cassandraKeyspace} WITH REPLICATION = " +
      s"{ 'class' : 'SimpleStrategy', 'replication_factor' : ${replicationFactor} }")
    session.execute(s"USE ${cassandraKeyspace}")

    createTables(session)
    session
  }

  def createTables(session: Session): ResultSet = {
    session.execute(s"CREATE TABLE IF NOT EXISTS tweets " +
      s"(tweetId bigint, createdAt bigint, userId bigint, " +
      s"tweetUserName text, countryName text, friendsCount bigint, " +
      s" PRIMARY KEY (tweetId, createdAt)) ")
  }

}

object CassandraConnection extends CassandraConnection
