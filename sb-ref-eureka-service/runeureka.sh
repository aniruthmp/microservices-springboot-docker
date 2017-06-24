#!/usr/bin/env bash

function note() {
    local GREEN NC
    GREEN='\033[0;32m'
    NC='\033[0m' # No Color
    printf "\n${GREEN}$@  ${NC}\n" >&2
}

set -e

title='eureka'
echo -n -e "\033]0;$title\007"

cd eureka-service
java -jar ./target/eureka-service-1.0.0-SNAPSHOT.jar
