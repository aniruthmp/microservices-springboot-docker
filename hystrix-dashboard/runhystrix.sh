#!/usr/bin/env bash

function note() {
    local GREEN NC
    GREEN='\033[0;32m'
    NC='\033[0m' # No Color
    printf "\n${GREEN}$@  ${NC}\n" >&2
}

set -e

title='hystrix'
echo -n -e "\033]0;$title\007"

cd hystrix-dashboard
java -jar ./target/hystrix-dashboard-1.0.0-SNAPSHOT.jar
