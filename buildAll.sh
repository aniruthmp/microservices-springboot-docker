#!/usr/bin/env bash


if [ "$#" -ne 1 ] ; then
  echo "Usage: ./buildAll.sh package (or) ./buildAll.sh install " >&2
  exit 1
fi

if [ $1 != "package" -a $1 != 'install' ] ; then
  echo "Usage: ./buildAll.sh package (or) ./buildAll.sh install " >&2
  exit 1
fi

cd C:/IdeaProjects/spring-cloud-microservices/sb-ref-auth-service
mvn clean $1 -DskipTests

cd C:/IdeaProjects/spring-cloud-microservices/sb-ref-config-service
mvn clean $1 -DskipTests

cd C:/IdeaProjects/spring-cloud-microservices/sb-ref-composite-service
mvn clean $1 -DskipTests

cd C:/IdeaProjects/spring-cloud-microservices/sb-ref-edge-service
mvn clean $1 -DskipTests

cd C:/IdeaProjects/spring-cloud-microservices/sb-ref-eureka-service
mvn clean $1 -DskipTests

cd C:/IdeaProjects/spring-cloud-microservices/sb-ref-hystrix-dashboard
mvn clean $1 -Dmaven.test.skip=true

cd C:/IdeaProjects/spring-cloud-microservices/sb-ref-reservation-service
mvn clean $1 -DskipTests

cd C:/IdeaProjects/spring-cloud-microservices/sb-ref-venue-service
mvn clean $1 -DskipTests

cd C:/IdeaProjects/spring-cloud-microservices/sb-ref-zipkin-service
mvn clean $1 -Dmaven.test.skip=true
