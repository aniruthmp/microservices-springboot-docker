#!/usr/bin/env bash
set -x
set -m

/entrypoint.sh couchbase-server &

sleep 15

# Setup index and memory quota
curl -v -X POST http://localhost:8091/pools/default -d memoryQuota=300 -d indexMemoryQuota=300

# Setup services
curl -v http://localhost:8091/node/controller/setupServices -d services=kv%2Cn1ql%2Cindex

# Setup credentials
curl -v http://localhost:8091/settings/web -d port=8091 -d username=Administrator -d password=password

# Setup Memory Optimized Indexes
curl -i -u Administrator:password -X POST http://localhost:8091/settings/indexes -d 'storageMode=memory_optimized'

# Create default bucket
curl -i -u Administrator:password -X POST http://localhost:8091/pools/default/buckets -d 'name=default' \
    -d 'ramQuotaMB=300' -d 'authType=none' -d 'replicaNumber=2' -d 'proxyPort=1025'

sleep 3

# Create default primary index
curl -i -u Administrator:password -X POST http://localhost:8093/query/service --data "statement=CREATE PRIMARY INDEX on default USING GSI"

echo "Type: $TYPE"

if [ "$TYPE" = "WORKER" ]; then
  sleep 15

  #IP=`hostname -s`
  IP=`hostname -I | cut -d ' ' -f1`

  echo "Auto Rebalance: $AUTO_REBALANCE"
  if [ "$AUTO_REBALANCE" = "true" ]; then
    couchbase-cli rebalance --cluster=$COUCHBASE_MASTER:8091 --user=Administrator --password=password --server-add=$IP --server-add-username=Administrator --server-add-password=password
  else
    couchbase-cli server-add --cluster=$COUCHBASE_MASTER:8091 --user=Administrator --password=password --server-add=$IP --server-add-username=Administrator --server-add-password=password
  fi;
fi;

fg 1