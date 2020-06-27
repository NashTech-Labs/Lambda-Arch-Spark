package com.knoldus.kafka.consumer

import java.util.Date

import com.datastax.driver.core.ResultSet

import scala.collection.JavaConversions._

case class TwitterUser(userId: Long, createdAt: Date, friendCount: Long)

case class Response(numberOfUser: Long, data: List[TwitterUser])

object CassandraOperation extends CassandraConnection {

  def insertTweets(listJson: List[String], tableName: String = "tweets"): List[ResultSet] = {
    listJson.map { json =>
      println(s"About to insert : $json")
      cassandraConn.execute(s"""INSERT INTO $tableName JSON '${json.replaceAll("'", "''")}'""")
    }
  }

  def findTwitterUsers(minute: Long, second: Long, tableName: String = "tweets"): Response = {
    val batchViewResult =
      if (minute > 0) {
        val currentTime = System.currentTimeMillis()
        val batchInterval = currentTime - minute * 60 * 1000
        cassandraConn.execute(
          s"select * from batch_view.friendcountview where createdat >= $batchInterval allow filtering;"
        ).all().toList
      } else {
        Nil
      }

    val realTimeViewResult = if (second > 0) {
      val currentTime = System.currentTimeMillis()
      val realTimeInterval = currentTime - second * 1000
      cassandraConn.execute(
        s"select * from realtime_view.friendcountview where createdat >= $realTimeInterval allow filtering;"
      ).all().toList
    } else {
      Nil
    }

    val twitterUsers = (batchViewResult ++ realTimeViewResult).map { row =>
      TwitterUser(row.getLong("userid"), new Date(row.getLong("createdat")), row.getLong("friendscount"))
    }

    Response(twitterUsers.length, twitterUsers)
  }

}
