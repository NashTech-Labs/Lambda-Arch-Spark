package com.knoldus.serving

import akka.http.scaladsl.server._
import com.knoldus.util.HttpUtil

trait SearchRestService extends HttpUtil with SearchRestServiceHandler {

  // ==============================
  //     REST ROUTES
  // ==============================

  def searchTwitterUserByFriendCount: Route = get {
    path("find" / "user") {
      parameters('min, 'sec) { (min: String, sec: String) =>
        logDuration(onSuccess(getTwitterUserByFriendCount(min, sec))(complete(_)))
      }
    }
  }

  def routes: Route = searchTwitterUserByFriendCount

}
