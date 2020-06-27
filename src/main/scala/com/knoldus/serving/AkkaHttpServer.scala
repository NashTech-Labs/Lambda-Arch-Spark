package com.knoldus.serving

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.fusesource.jansi.Ansi.Color._
import org.fusesource.jansi.Ansi._

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object AkkaHttpServer extends App with SearchRestService {

  implicit val system: ActorSystem = ActorSystem("search")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher


  val binding = Http().bindAndHandle(routes, "localhost", 9000)

  //scalastyle:off
  binding.onComplete {
    case Success(binding) ⇒
      val localAddress = binding.localAddress
      println(ansi().fg(GREEN).a(
        """
            ___   ___     _     ___    ___   _  _
           / __| | __|   /_\   | _ \  / __| | || |
           \__ \ | _|   / _ \  |   / | (__  | __ |
           |___/ |___| /_/ \_\ |_|_\  \___| |_||_|

        """
      ).reset())
      //scalastyle:on

      info(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
    case Failure(e) ⇒
      logger.info(s"Binding failed with ${e.getMessage}")
      system.terminate()
  }
}
