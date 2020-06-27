package com.knoldus.kafka.producer

import java.util.concurrent.LinkedBlockingQueue

import com.knoldus.util.{JsonHelper, KafkaCassandraConfigUtil}
import twitter4j._
import twitter4j.conf.ConfigurationBuilder

case class Tweet(tweetId: Long, createdAt: Long, userId: Long, tweetUserName: String, countryName: String, friendsCount: Long)

object TwitterStreamApp extends App with JsonHelper{


  val queue = new LinkedBlockingQueue[Status](1000)

  val boundingBox: Array[Double] = Array(-180.0,-90.0)
  val boundingBox1: Array[Double] = Array(180.0,90.0)

  val keyWords = "covid19"

  val cb = new ConfigurationBuilder()
  cb.setDebugEnabled(true)
    .setOAuthConsumerKey(KafkaCassandraConfigUtil.consumerKey)
    .setOAuthConsumerSecret(KafkaCassandraConfigUtil.consumerSecret)
    .setOAuthAccessToken(KafkaCassandraConfigUtil.accessToken)
    .setOAuthAccessTokenSecret(KafkaCassandraConfigUtil.accessTokenSecret)

  val twitterStream = new TwitterStreamFactory(cb.build()).getInstance()
  var counter = 0
  val listener = new StatusListener() {

    override def onStatus(status: Status) {

      queue.offer(status)
      counter = counter + 1
      val user = status.getUser
      val tweetId = status.getId
      val userName = user.getName.replaceAll("'","''")
      val message = write(Tweet(tweetId,status.getCreatedAt.getTime,user.getId,userName,status.getPlace.getCountry,user.getFriendsCount))
      KafkaTwitterProducer.send("tweets",message)
      println(s" tweet text is ::::   ${message}  counter ::: ${counter}")

    }

    override def onDeletionNotice(statusDeletion_Notice: StatusDeletionNotice): Unit = {}

    override def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {
      // System.out.println("Got track limitation notice:" +num-berOfLimitedStatuses);
    }

    override def onScrubGeo(userId: Long, upToStatusId: Long) {
      // System.out.println("Got scrub_geo event userId:" + userId +"upToStatusId:" + upToStatusId);
    }

    override def onStallWarning(warning: StallWarning) {
      // System.out.println("Got stall warning:" + warning);
    }

    override def onException(ex: Exception) {
      ex.printStackTrace()
    }
  }

  twitterStream.addListener(listener)

  val query = new FilterQuery().track(keyWords)
  //query.
  query.locations(boundingBox,boundingBox1)
  twitterStream.filter(query)

  Thread.sleep(100000000)

}
