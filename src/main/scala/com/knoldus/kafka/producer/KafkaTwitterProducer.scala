package com.knoldus.kafka.producer

import java.util.{Properties, Random}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.LoggerFactory

object KafkaTwitterProducer {

  val logger = LoggerFactory.getLogger(this.getClass)
  private val props = new Properties()
  val maxRetries:Integer = 3
  val batchSize :Integer= 1638
  val lingerTime :Integer= 1
  val bufferSize :Integer= 33554432
  val rnd = new Random
  props.put("bootstrap.servers", "localhost:9092")
  props.put("acks", "all")
  props.put("retries", maxRetries)
  props.put("batch.size", batchSize)
  props.put("linger.ms", lingerTime)
  props.put("buffer.memory", bufferSize)
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  /**
    * Send message to kafka queue
    *
    * @param topic
    * @param message
    * @param mode
    * @return
    */
  def send(topic: String, message: String, mode: String = "default"): Boolean = {
    try {
      println("sending message  to kafka queue.......")
      val partition = rnd.nextInt(4).toString
       producer.send(new ProducerRecord[String, String](topic, partition, message))
      true
    } catch {
      case ex: Exception =>
        logger.error("Getting exception to sending message", ex)
        false
    }
  }

}

