package com.knoldus.speed

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.streaming.Time
import org.apache.spark.streaming.dstream.DStream

/**
  * Created by narayan on 12/11/16.
  */
object ViewHandler {


  def createAllView(sparkContext: SparkContext, tweets: DStream[String]) = {
    createViewForFriendCount(sparkContext, tweets)
  }

  def createViewForFriendCount(sparkContext: SparkContext, tweets: DStream[String]) = {

    tweets.foreachRDD { (rdd: RDD[String], time: Time) =>
      val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
      val tweets: DataFrame = spark.sqlContext.read.json(rdd)
      tweets.createOrReplaceTempView("tweets")
      val wordCountsDataFrame: DataFrame = spark.sql("SELECT userId,createdAt, friendsCount from tweets Where friendsCount > 500 ")
      val res: DataFrame = wordCountsDataFrame.withColumnRenamed("userId","userid").withColumnRenamed("createdAt","createdat").withColumnRenamed("friendsCount","friendscount")
      res.write.mode(SaveMode.Append)
        .format("org.apache.spark.sql.cassandra")
        .options(Map( "table" -> "friendcountview", "keyspace" -> "realtime_view"))
        .save()
      wordCountsDataFrame.show(false)
      wordCountsDataFrame.printSchema()

    }
  }
}
