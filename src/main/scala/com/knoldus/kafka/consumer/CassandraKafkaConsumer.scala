package com.knoldus.kafka.consumer

import java.util.Properties

import scala.collection.JavaConversions._
import akka.actor._
import org.apache.kafka.clients.consumer.ConsumerRecord

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.FiniteDuration

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import scala.concurrent.duration.DurationInt

class KafkaMessageConsumer {

  private val props = new Properties
  props.put("group.id", "batch_consumer")
  props.put("bootstrap.servers", "localhost:9092")
  props.put("zookeeper.connect", "localhost:2181")
  props.put("enable.auto.commit", "true")
  props.put("consumer.timeout.ms", "500")
  props.put("auto.commit.interval.ms", "1000")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  private val noOfStreams = 1
  private val batchSize = 100
  private val topic = "tweets"
  private val consumerConnector = new KafkaConsumer[String, String](props)

  consumerConnector.subscribe(java.util.Collections.singletonList(topic))

  def readAndProcess(): Unit = {
    val records = consumerConnector.poll(java.time.Duration.ofSeconds(1))
    records.iterator().foreach { record =>
      CassandraOperation.insertTweets(List(record.value()))
    }
  }

}

case object GiveMeWork

class KafkaMessageConsumerActor(consumer: KafkaMessageConsumer) extends Actor  {

  implicit val dispatcher: ExecutionContextExecutor = context.dispatcher

  val initialDelay: FiniteDuration = 1000.milli
  val interval: FiniteDuration = 1.seconds


  context.system.scheduler.schedule(initialDelay, interval, self, GiveMeWork)

  def receive: Receive = {
    case GiveMeWork => consumer.readAndProcess()
  }

}

object CassandraKafkaConsumer extends App {

  val actorSystem = ActorSystem("KafkaActorSystem")

  val consumer = actorSystem.actorOf(Props(new KafkaMessageConsumerActor(new KafkaMessageConsumer)))

  consumer ! GiveMeWork

}
