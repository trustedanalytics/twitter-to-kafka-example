#
# Copyright (c) 2016 Intel Corporation
#
# Licensed under the Apache License, Version 2.0 (the 'License');
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an 'AS IS' BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

"""
This scripts automates deployment of twitter-to-kafka-example application
(creates required service instances, configures environment variables).
"""

import json

from app_deployment_helpers import cf_cli
from app_deployment_helpers import cf_helpers

APP_NAME = "twitter-to-kafka-example"

PARSER = cf_helpers.get_parser(APP_NAME)
PARSER.add_argument('-k','--twitter_consumer_key', type=str, required=True, help='Your application''s Consumer Key '
                                                                                 'used to authenticate '
                                                                                 'requests to the Twitter Platform')
PARSER.add_argument('-cs','--twitter_consumer_secret', type=str, required=True, help='Your application''s Secret '
                                                                                     'used to authenticate requests to '
                                                                                     'the Twitter Platform')
PARSER.add_argument('-t','--twitter_token', type=str, required=True, help='Twitter Token that can be used to make API '
                                                                          'requests on your own account''s behalf')
PARSER.add_argument('-s','--twitter_secret', type=str, required=True, help='Twitter Secret that can be used to make API '
                                                                           'requests on your own account''s behalf.')
PARSER.add_argument('-tm','--twitter_terms', type=str, required=True, help='https://dev.twitter.com/streaming/'
                                                                           'overview/request-parameters#track')
PARSER.add_argument('-tf','--twitter_followings', type=str, default="", help='https://dev.twitter.com/streaming/'
                                                                             'overview/request-parameters#follow')
PARSER.add_argument('-tl','--twitter_locations', type=str, default="", help='https://dev.twitter.com/streaming/'
                                                                            'overview/request-parameters#locations')
PARSER.add_argument('-kt','--kafka_topic', type=str, required=True, help='The name of the kafka topic for the '
                                                                         'application to received tweets')
ARGS = PARSER.parse_args()

CF_INFO = cf_helpers.get_info(ARGS)
cf_cli.login(CF_INFO)

cf_cli.create_service('kafka', 'shared', 'kafka-twitter-instance')

PROJECT_DIR = ARGS.project_dir if ARGS.project_dir else \
    cf_helpers.get_project_dir()
cf_helpers.prepare_package(work_dir=PROJECT_DIR)
cf_helpers.push(work_dir=PROJECT_DIR, options="{0} -n {0} --no-start".format(ARGS.app_name))

cf_cli.set_env(APP_NAME, "TWITTER_CONSUMER_KEY", ARGS.twitter_consumer_key)
cf_cli.set_env(APP_NAME, "TWITTER_CONSUMER_SECRET", ARGS.twitter_consumer_secret)
cf_cli.set_env(APP_NAME, "TWITTER_TOKEN", ARGS.twitter_token)
cf_cli.set_env(APP_NAME, "TWITTER_SECRET", ARGS.twitter_secret)
cf_cli.set_env(APP_NAME, "TWITTER_TERMS", ARGS.twitter_terms)
cf_cli.set_env(APP_NAME, "TWITTER_FOLLOWINGS", ARGS.twitter_followings)
cf_cli.set_env(APP_NAME, "TWITTER_LOCATIONS", ARGS.twitter_locations)
cf_cli.set_env(APP_NAME, "KAFKA_TOPIC", ARGS.kafka_topic)

cf_cli.start(APP_NAME)