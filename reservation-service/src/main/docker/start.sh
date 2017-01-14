#!/bin/bash

# wait for 15 seconds until config server is up
./wait-for-it.sh -t 15 configserver:8888

if [ $? -eq 0 ]
then
  # To reduce Tomcat startup time we added a system property pointing to "/dev/urandom" as a source of entropy.
  java -Djava.security.egd=file:/dev/./urandom -jar app.jar
fi