package com.knoldus.speed

import org.apache.commons.codec.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext, Time}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object SparkStreamingKafkaConsumer {

  def main(array: Array[String]): Unit = {

    val brokers = "localhost:9092"

    val sparkConf = new SparkConf().setAppName("KafkaDirectStreaming").setMaster("local[2]")
      .set("spark.cassandra.connection.host", "127.0.0.1")
      .set("spark.cassandra.auth.username", "cassandra")
    val ssc = new StreamingContext(sparkConf, Seconds(10))
    ssc.checkpoint("checkpointDir")

    val topicsSet = Set("tweets")
    val kafkaParams = Map[String, Object]("metadata.broker.list" -> brokers, "group.id" -> "spark_streaming",
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer], "value.deserializer" -> classOf[StringDeserializer])
    val messages: InputDStream[ConsumerRecord[String, String]] =
      KafkaUtils.createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String, String](topicsSet, kafkaParams))

    val tweets: DStream[String] = messages.map(record => record.value)

    val spark = SparkSession.builder.config(sparkConf).getOrCreate()
    ViewHandler.createAllView(spark, tweets)
    ssc.start()
    ssc.awaitTermination()

  }

}
