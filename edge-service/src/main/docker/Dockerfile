FROM frolvlad/alpine-oraclejdk8:slim

# add bash and coreutils
RUN apk add --no-cache bash coreutils

# We added a VOLUME pointing to "/tmp" because that is where a Spring Boot application creates working directories for
# Tomcat by default. The effect is to create a temporary file on your host under "/var/lib/docker" and link it to the
# container under "/tmp". This step is optional for the simple app that we wrote here, but can be necessary for other
# Spring Boot applications if they need to actually write in the filesystem.
VOLUME /tmp

ADD edge-service-*.jar app.jar
# script files related to check if the datastore is “ready”
ADD wait-for-it.sh wait-for-it.sh
ADD start.sh start.sh

RUN chmod 755 wait-for-it.sh
RUN chmod 755 start.sh

EXPOSE 9999

# You can use a RUN command to "touch" the jar file so that it has a file modification time
# (Docker creates all container files in an "unmodified" state by default)
# This actually isn’t important for the simple app that we wrote, but any static content (e.g. "index.html")
# would require the file to have a modification time.
RUN bash -c 'touch /app.jar'

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

CMD ["./start.sh"]
