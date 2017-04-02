# Lambda-Arch-Spark
In this project we are trying to analyse twitter's tweets using lambda architecture.

-----------------------------------------------------------------------
#### What is Lambda architecture ?
-----------------------------------------------------------------------
Lambda architecture is a data processing architecture designed to handle massive quantities of data by taking advantage of both batch and stream processing methods.
For more details please check [Twitter's tweets analysis using Lambda Architecture](https://blog.knoldus.com/2017/01/31/twitters-tweets-analysis-using-lambda-architecture/)

-----------------------------------------------------------------------
### Now Play
-----------------------------------------------------------------------
* Clone the project into local system : `$ git clone git@github.com:knoldus/Lambda-Arch-Spark.git` 
* Akka requires that you have [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or later installed   on your machine.
* Install SBT if you do not have
* Install Kafka
* Install Cassandra
* We need to create twitter app to access twitter realtime tweets.
* We need to put twitter's app consumerKey,consumerSecret,accessToken and accessTokenSecret into application.conf file of       this   project.
* Before start the project we need to start kafka and cassandra.
* Execute `sbt clean compile` to build the product
* Execute `sbt run` to execute the project it will show you multiple option.
* We need to  first start **TwitterStreamApp** to fetch tweets from twitter, then start **CassandraKafkaConsumer** which is     responsible for fetch data from kafka and put into master dataset.After that we can start **SparkStreamingKafkaConsumer**     for realtime view and **BatchProcessor** for batch view.There is onther app **AkkaHttpServer** which is responsible for       serving layer.Basically it merges realtime and batch view against pre specified query and retrun result back to web client.

-----------------------------------------------------------------------
### References
-----------------------------------------------------------------------
* [Akka HTTP](http://doc.akka.io/docs/akka/2.4.7/scala/http/index.html)
* [Scala](http://scala-lang.org/)
* [Apache Spark](http://spark.apache.org/)
* [Apache Spark Streaming](http://spark.apache.org/docs/latest/streaming-programming-guide.html)
* [Apache Cassandra](http://cassandra.apache.org/)
* [Apache Kafka](https://kafka.apache.org/)
