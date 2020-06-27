package com.knoldus.serving

import akka.util.Timeout
import com.knoldus.kafka.consumer.CassandraOperation
import com.knoldus.util.{JsonHelper, LoggerUtil}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

trait SearchRestServiceHandler extends JsonHelper  with LoggerUtil {

  implicit val timeout: Timeout = Timeout(40.seconds)

  def getTwitterUserByFriendCount(minute: String, second: String): Future[String] = {
    Future(write(CassandraOperation.findTwitterUsers(minute.toLong,second.toLong)))
  }

}

object SearchRestServiceHandler extends SearchRestServiceHandler

