package com.knoldus.speed

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.streaming.Time
import org.apache.spark.streaming.dstream.DStream

object ViewHandler {

  def createAllView(spark: SparkSession, tweets: DStream[String]): Unit = createViewForFriendCount(spark, tweets)

  def createViewForFriendCount(spark: SparkSession, tweets: DStream[String]): Unit = {
    tweets.foreachRDD { (rdd: RDD[String], time: Time) =>
      val tweets: DataFrame = spark.sqlContext.read.json(rdd)
      tweets.createOrReplaceTempView("tweets")
      val wordCountsDataFrame: DataFrame = spark.sql("SELECT userId,createdAt, friendsCount from tweets Where friendsCount > 500 ")
      val res: DataFrame = wordCountsDataFrame.withColumnRenamed("userId","userid")
        .withColumnRenamed("createdAt","createdat").withColumnRenamed("friendsCount","friendscount")

      res.write.mode(SaveMode.Append)
        .format("org.apache.spark.sql.cassandra")
        .options(Map( "table" -> "friendcountview", "keyspace" -> "realtime_view"))
        .save()
      wordCountsDataFrame.show(false)
      wordCountsDataFrame.printSchema()
    }
  }
}
