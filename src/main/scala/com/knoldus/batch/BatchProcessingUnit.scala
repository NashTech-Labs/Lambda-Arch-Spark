package com.knoldus.batch

import akka.actor.{Actor, ActorSystem, Props}
import com.datastax.spark.connector._
import org.apache.spark.{SparkConf, SparkContext}

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.FiniteDuration

class BatchProcessingUnit {

  val sparkConf: SparkConf = new SparkConf().setAppName("Lambda_Batch_Processor").setMaster("local")
    .set("spark.cassandra.connection.host", "localhost")
    .set("spark.cassandra.auth.username", "cassandra")
    .set("spark.driver.allowMultipleContexts", "true")
  val sc = new SparkContext(sparkConf)

  def start: Unit = {

    val rdd = sc.cassandraTable("master_dataset", "tweets")
    val result = rdd.select("userid", "createdat", "friendscount").where("friendsCount > ?", 500)
    result.saveToCassandra("batch_view", "friendcountview", SomeColumns("userid", "createdat", "friendscount"))
    result.foreach(println)
  }

}

import scala.concurrent.duration.DurationInt

case object StartBatchProcess

class BatchProcessingActor(processor: BatchProcessingUnit) extends Actor {

  implicit val dispatcher: ExecutionContextExecutor = context.dispatcher

  val initialDelay: FiniteDuration = 1000.milli
  val interval: FiniteDuration = 60.seconds


  context.system.scheduler.schedule(initialDelay, interval, self, StartBatchProcess)

  def receive: Receive = {
    case StartBatchProcess => processor.start
  }

}

object BatchProcessor extends App {

  val actorSystem = ActorSystem("BatchActorSystem")

  val processor = actorSystem.actorOf(Props(new BatchProcessingActor(new BatchProcessingUnit)))

  processor ! StartBatchProcess

}
