FROM alpine:3.5

# A few problems with compiling Java from source:
#  1. Oracle.  Licensing prevents us from redistributing the official JDK.
#  2. Compiling OpenJDK also requires the JDK to be installed, and it gets
#       really hairy.

# Default to UTF-8 file.encoding
ENV LANG C.UTF-8

# add a simple script that can auto-detect the appropriate JAVA_HOME value
# based on whether the JDK or only the JRE is installed
RUN { \
		echo '#!/bin/sh'; \
		echo 'set -e'; \
		echo; \
		echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"'; \
	} > /usr/local/bin/docker-java-home \
	&& chmod +x /usr/local/bin/docker-java-home
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV PATH $PATH:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin

ENV JAVA_VERSION 8u111
ENV JAVA_ALPINE_VERSION 8.121.13-r0

RUN set -x \
	&& apk add --no-cache \
		openjdk8="$JAVA_ALPINE_VERSION" \
	&& [ "$JAVA_HOME" = "$(docker-java-home)" ]

# Section above will be replaced with a single FROM once we have it in artifactitory
####################################################################################

# add bash and coreutils curl jq
RUN apk add --update --no-cache bash coreutils curl jq && \
    rm -rf /var/cache/apk/*

# We added a VOLUME pointing to "/tmp" because that is where a Spring Boot application creates working directories for
# Tomcat by default. The effect is to create a temporary file on your host under "/var/lib/docker" and link it to the
# container under "/tmp". This step is optional for the simple app that we wrote here, but can be necessary for other
# Spring Boot applications if they need to actually write in the filesystem.

VOLUME /tmp

ADD *.jar app.jar
ADD git.properties git.properties
ADD build-info.properties build-info.properties

# script files related to check if the datastore is “ready”
ADD wait-for-it.sh wait-for-it.sh
ADD start.sh start.sh

# Change permissions for wait-for-it.sh, start.sh, touch the app.jar, add the appuser group, add appuser to the appuser group, make log folder,
# change owner for log folder to appuser group

RUN chmod 755 wait-for-it.sh \
    && chmod 755 start.sh \
    && bash -c 'touch /app.jar' \
    && addgroup -S appuser \
    && adduser -S -g appuser appuser \
    && mkdir ./log \
    && chown appuser:appuser ./log

USER appuser

# This will override the HTTP port to listen on
# May be set by service definition
ENV SERVER_PORT 8080
EXPOSE ${SERVER_PORT}

# Wired Docker healthcheck to Springboot Actuator for Health Check
HEALTHCHECK --interval=30s --timeout=10s --retries=10 CMD curl --fail http://localhost:${SERVER_PORT}/admin/health || exit 1

CMD ["./start.sh"]
