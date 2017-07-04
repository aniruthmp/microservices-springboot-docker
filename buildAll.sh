#!/usr/bin/env bash


if [ "$#" -ne 1 ] ; then
  echo "Usage: ./buildAll.sh package (or) ./buildAll.sh install " >&2
  exit 1
fi

if [ $1 != "package" -a $1 != 'install' ] ; then
  echo "Usage: ./buildAll.sh package (or) ./buildAll.sh install " >&2
  exit 1
fi

cd /Users/a.c.parthasarathy/ideaProjects/microservices-springboot-docker/sb-ref-auth-service
mvn clean $1 -DskipTests

cd /Users/a.c.parthasarathy/ideaProjects/microservices-springboot-docker/sb-ref-config-service
mvn clean $1 -DskipTests

cd /Users/a.c.parthasarathy/ideaProjects/microservices-springboot-docker/sb-ref-composite-service
mvn clean $1 -DskipTests

cd /Users/a.c.parthasarathy/ideaProjects/microservices-springboot-docker/sb-ref-edge-service
mvn clean $1 -DskipTests

cd /Users/a.c.parthasarathy/ideaProjects/microservices-springboot-docker/sb-ref-eureka-service
mvn clean $1 -DskipTests

cd /Users/a.c.parthasarathy/ideaProjects/microservices-springboot-docker/sb-ref-hystrix-dashboard
mvn clean $1 -Dmaven.test.skip=true

cd /Users/a.c.parthasarathy/ideaProjects/microservices-springboot-docker/sb-ref-reservation-service
mvn clean $1 -DskipTests

cd /Users/a.c.parthasarathy/ideaProjects/microservices-springboot-docker/sb-ref-venue-service
mvn clean $1 -DskipTests

cd /Users/a.c.parthasarathy/ideaProjects/microservices-springboot-docker/sb-ref-zipkin-service
mvn clean $1 -Dmaven.test.skip=true
