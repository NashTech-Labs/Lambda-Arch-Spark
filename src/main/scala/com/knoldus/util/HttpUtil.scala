package com.knoldus.util

import akka.http.scaladsl.server._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{native, DefaultFormats, Formats}

/**
 * This object provides utility for HTTP routes
 */
trait HttpUtil extends Directives with Json4sSupport with LoggerUtil {

  implicit val serialization = native.Serialization

  implicit def json4sFormats: Formats = DefaultFormats

  val rejectionHandler = RejectionHandler.default


  def logDuration(inner: Route): Route = { ctx =>
    val start = System.currentTimeMillis()
    // handling rejections here so that we get proper status codes
    val innerRejectionsHandled = handleRejections(rejectionHandler)(inner)
    mapResponse { resp =>
      val d = System.currentTimeMillis() - start
      info(s"[${resp.status.intValue()}] ${ctx.request.method.name} ${ctx.request.uri} took: ${d}ms")
      resp
    }(innerRejectionsHandled)(ctx)
  }

}
