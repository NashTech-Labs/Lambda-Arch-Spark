package com.knoldus.kafka.consumer


import java.util.Properties

import akka.actor._
import kafka.consumer.{Consumer, ConsumerConfig, ConsumerIterator, ConsumerTimeoutException}

class KafkaConsumer {


  private val props = new Properties
  props.put("group.id", "batch_consumer")
  //props.put("bootstrap.servers", "localhost:9092")
  props.put("zookeeper.connect", "localhost:2181")
  props.put("enable.auto.commit", "true")
  props.put("auto.offset.reset", "smallest")
  props.put("consumer.timeout.ms", "500")
  props.put("auto.commit.interval.ms", "1000")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  private val noOfStreams = 1
  private val batchSize = 100
  private val topic = "tweets"
  private val consumerConnector = Consumer.create(new ConsumerConfig(props))

  private val iterator: ConsumerIterator[Array[Byte], Array[Byte]] = consumerConnector.createMessageStreams(Map(topic -> noOfStreams)).mapValues(_.head)(topic).iterator()

  def read =
    try {
      if (iterator.hasNext()) {
        println("Got message   ::::::::::::::::::: ")
        readBatchFromTopic(topic, iterator)
      }
    } catch {
      case timeOutEx: ConsumerTimeoutException =>
        println("$$$Getting time out  when reading message")
      case ex: Exception =>
        println(s"Not getting message from ", ex)
    }


  private def readBatchFromTopic(topic: String, iterator: ConsumerIterator[Array[Byte], Array[Byte]]) = {
    var batch = List.empty[String]
    while (hasNext(iterator) && batch.size < batchSize) {
      batch = batch :+ (new String(iterator.next().message()))
    }
    if (batch.isEmpty) throw new IllegalArgumentException(s"$topic is  empty")else{ CassandraOperation.insertTweets(batch)}
    println(s"consumed batch into cassandra ::::::::::: ${batch.length}         ${batch.apply(0)}")
  }

  private def hasNext(it: ConsumerIterator[Array[Byte], Array[Byte]]): Boolean =
    try
      it.hasNext()
    catch {
      case timeOutEx: ConsumerTimeoutException =>
        println("Getting time out  when reading message :::::::::::::: ")
        false
      case ex: Exception =>
        println("Getting error when reading message :::::::::::::::::  ", ex)
        false
    }

}

import scala.concurrent.duration.DurationInt

case object GiveMeWork

class KafkaMessageConsumer(consumer: KafkaConsumer) extends Actor  {

  implicit val dispatcher = context.dispatcher

  val initialDelay = 1000 milli
  val interval = 1 seconds


  context.system.scheduler.schedule(initialDelay, interval, self, GiveMeWork)

  def receive: PartialFunction[Any, Unit] = {

    case GiveMeWork => consumer.read
  }

}

object CassandraKafkaConsumer extends App {

  val actorSystem = ActorSystem("KafkaActorSystem")

  val consumer = actorSystem.actorOf(Props(new KafkaMessageConsumer(new KafkaConsumer)))

  consumer ! GiveMeWork

}
