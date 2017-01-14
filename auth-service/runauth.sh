#!/usr/bin/env bash

function note() {
    local GREEN NC
    GREEN='\033[0;32m'
    NC='\033[0m' # No Color
    printf "\n${GREEN}$@  ${NC}\n" >&2
}

set -e

export EDGE_SERVICE_SECRET=edgesecret
export RESERVATION_SERVICE_SECRET=reservationsecret
export VENUE_SERVICE_SECRET=venuesecret

title='auth'
echo -n -e "\033]0;$title\007"

cd auth-service
java -jar ./target/auth-service-1.0.0-SNAPSHOT.jar
