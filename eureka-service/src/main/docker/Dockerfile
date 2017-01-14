FROM frolvlad/alpine-oraclejdk8:slim

EXPOSE 8761

VOLUME /tmp
ADD eureka-service-*.jar app.jar
RUN sh -c 'touch /app.jar'

# Create empty log file
RUN mkdir ./log
RUN bash -c 'touch ./log/application.log'
RUN chmod 666 ./log/application.log

#By default docker containers run as root. A docker container running as root has full control of the host system. 
#Requiring root is dangerous and may not be available in all environments. Your image should use the USER instruction 
#to specify a non-root user for containers to run as. If your software does not create its own user, you can create a 
#user and group in the Dockerfile as follows
RUN addgroup -S appuser && adduser -S -g appuser appuser
USER appuser

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
