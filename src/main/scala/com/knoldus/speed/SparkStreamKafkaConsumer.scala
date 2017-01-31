package com.knoldus.speed

import _root_.kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils


object SparkStreamingKafkaConsumer extends App {

  val brokers = "localhost:9092"

  val sparkConf = new SparkConf().setAppName("KafkaDirectStreaming").setMaster("local[2]")
    .set("spark.cassandra.connection.host", "127.0.0.1")
    .set("spark.cassandra.auth.username", "cassandra")
  val ssc = new StreamingContext(sparkConf, Seconds(10))
  ssc.checkpoint("checkpointDir")

  val topicsSet = Set("tweets")
  val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers, "group.id" -> "spark_streaming")
  val messages: InputDStream[(String, String)] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

  val tweets: DStream[String] = messages.map { case (key, message) => message }
  ViewHandler.createAllView(ssc.sparkContext, tweets)
  ssc.start()
  ssc.awaitTermination()
}

