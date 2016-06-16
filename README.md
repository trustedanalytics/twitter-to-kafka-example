# twitter-to-kafka-example
A simple ingestion of Twitter messages into Kafka queue on TAP.

## Configuring the application
This simple example uses application.properties to provide some configuration data.

|key                                          | description|
|---                                          |---|
|twitter.consumer.key, twitter.consumer.secret| Your application's Consumer Key and Secret are used to authenticate requests to the Twitter Platform.|
|twitter.token, twitter.secret                | Access token/secret can be used to make API requests on your own account's behalf.|
|twitter.terms                                | https://dev.twitter.com/streaming/overview/request-parameters#track |
|twitter.followings                           | https://dev.twitter.com/streaming/overview/request-parameters#follow|
|twitter.locations                            | https://dev.twitter.com/streaming/overview/request-parameters#locations|
|kafka.topic                                  | The name of the kafka topic for the application to received tweets. |
|kafka.brokers                                | A list of host/port pairs to use for establishing the initial connection to the Kafka cluster.|


To configure logging the following log4j properties can be added to the application.properties file:

    logging.level.ROOT=INFO
    logging.level.org.trustedanalytics=DEBUG


## Deploying to Cloud Foundry
To deploy this application to Cloud Foundry you could use the following manifest file (after putting your own configuration data):

    ---
    applications:
    - name: twitter-to-kafka
      memory: 1G
      instances: 1
      host: twitter-to-kafka
      path: target/twitter-to-kafka-0.0.2-SNAPSHOT.jar
      services:
      - kafka-twitter-instance
      env:
        TWITTER_CONSUMER_KEY: <<your data goes here>>
        TWITTER_CONSUMER_SECRET: <<your data goes here>>
        TWITTER_TOKEN: <<your data goes here>>
        TWITTER_SECRET: <<your data goes here>>
        TWITTER_TERMS: <<your data goes here; escape hashes>>
        TWITTER_FOLLOWINGS: <<your data goes here>>
        TWITTER_LOCATIONS: <<your data goes here>>
        KAFKA_TOPIC: <<your data goes here>>


## Local development

For local development Zookeeper and Kafka must be installed.

### Zookeeper

Zookeeper can be downloaded from Apache ZooKeeper Releases

I assume you are working on Linux machine and will be using twitter-to-kafka-example directory as a workspace.

Simple installation could look like this:

    cd ~/twitter-to-kafka-example
    mkdir zookeeper
    cd zookeeper
    wget http://apache.mirrors.lucidnetworks.net/zookeeper/zookeeper-3.4.6/zookeeper-3.4.6.tar.gz
    tar -xvf zookeeper-3.4.6.tar.gz
    cd zookeeper-3.4.6/

To test the installation:

    cp conf/zoo_sample.cfg conf/zoo.cfg
    bin/zkServer.sh start

You should see something similar to:

    JMX enabled by default
    Using config: /home/ubuntu/zookeeper-3.4.6/bin/../conf/zoo.cfg
    Starting zookeeper ... STARTED

### Kafka

Kafka downloads ara available here: http://kafka.apache.org/downloads.html. The current stable version is 0.10.0.0.

I pick version kafka_2.11-0.10.0.0.tgz from the following mirror: http://mirrors.sonic.net/apache/kafka/0.10.0.0/kafka_2.11-0.10.0.0.tgz

    cd ~/twitter-to-kafka-example
    mkdir kafka
    cd kafka
    wget http://mirrors.sonic.net/apache/kafka/0.8.2.1/kafka_2.11-0.10.0.0.tgz
    tar xvzf kafka_2.11-0.10.0.0.tgz  

To test if it works, start the server:

    cd kafka_2.11-0.10.0.0
    bin/kafka-server-start.sh config/server.properties


### Building and running

To run the example as a local standalone application you can execute the following command:

    mvn clean package
    mvn spring-boot:run


## Creating Twitter application

The Twitter keys and tokens must be taken from the corresponding Twitter application.
To create a Twitter application a Twitter account must be created first.

Once the Twitter account is created the following steps must be performed in Twitter:

1. Go to https://apps.twitter.com/ 
2. Click on "Create New App".
3. Enter application mandatory data like name, description and website.
4. Click "Create your Twitter application".
5. After the app has been created go to the tab "Keys and Access Tokens".
7. Click "Create my access token".
8. Copy all keys and tokens from this tab as they are needed to configure twitter-to-kafka-example.  

These are the necessary steps to get the required tokens. 
Jump to the configuration section to figure out how to configure the example application correctly using the tokens and the keys. 

Keep in mind that:
* The access token can be used to make API requests on your own account's behalf. 
* Do not share your access token secret with anyone.
* Keep the "Consumer Secret" a secret. This key should never be human-readable in your application.
