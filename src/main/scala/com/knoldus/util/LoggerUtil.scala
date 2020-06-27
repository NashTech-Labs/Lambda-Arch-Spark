package com.knoldus.util

import org.slf4j.{ Logger, LoggerFactory }

trait LoggerUtil {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  def info(message: String): Unit = logger.info(message)

  def error(message: String, exception: Throwable): Unit = logger.error(message + " Reason::" + exception.getCause)

}

object LoggerUtil extends LoggerUtil
