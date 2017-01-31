package com.knoldus.batch

import akka.actor.{Actor, ActorSystem, Props}
import com.datastax.spark.connector._
import org.apache.spark.{SparkConf, SparkContext}

class BatchProcessingUnit {

  val sparkConf = new SparkConf()
    .setAppName("Lambda_Batch_Processor").setMaster("local[2]")
    .set("spark.cassandra.connection.host", "127.0.0.1")
    .set("spark.cassandra.auth.username", "cassandra")

  val sc = new SparkContext(sparkConf)

  def start: Unit ={
    val rdd = sc.cassandraTable("master_dataset", "tweets")
    val result = rdd.select("userid","createdat","friendscount").where("friendsCount > ?", 500)
    result.saveToCassandra("batch_view","friendcountview",SomeColumns("userid","createdat","friendscount"))
    result.foreach(println)
  }



}

import scala.concurrent.duration.DurationInt

case object StartBatchProcess

class BatchProcessingActor(processor: BatchProcessingUnit) extends Actor  {

  implicit val dispatcher = context.dispatcher

  val initialDelay = 1000 milli
  val interval = 60 seconds


  context.system.scheduler.schedule(initialDelay, interval, self, StartBatchProcess)

  def receive: PartialFunction[Any, Unit] = {

    case StartBatchProcess => processor.start

  }

}

object BatchProcessor extends App {

  val actorSystem = ActorSystem("BatchActorSystem")

  val processor = actorSystem.actorOf(Props(new BatchProcessingActor(new BatchProcessingUnit)))

  processor ! StartBatchProcess

}
