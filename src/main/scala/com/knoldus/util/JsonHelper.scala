package com.knoldus.util


import net.liftweb.json.{ DefaultFormats, JNothing, JValue, Serialization, parse => liftParser }

trait JsonHelper {

  implicit protected val formats = DefaultFormats

  protected def write[T <: AnyRef](value: T): String = Serialization.write(value)

  protected def parse(value: String): JValue = liftParser(value)

  implicit protected def extractOrEmptyString(json: JValue): String = json match {
    case JNothing => ""
    case data => data.extract[String]
  }

}
