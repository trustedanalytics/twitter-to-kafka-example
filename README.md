# twitter-to-kafka-example
A simple ingestion of Twitter messages into Kafka queue on TAP

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


## Deploying to cloud foundry

### Manual deployment
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

### Automated deployment
* Switch to `deploy` directory: `cd deploy`
* Install tox: `sudo -E pip install --upgrade tox`
* Run: `tox`
* Activate virtualenv with installed dependencies: `. .tox/py27/bin/activate`
* Run deployment script: `python deploy.py` providing required parameters when running script (`python deploy.py -h` to check script parameters with their descriptions).


# TODO
* check how the app behaves when kafka is down
* update readme
