#!/bin/bash

# Getting your cmdb app
wget https://github.com/chrisvugrinec/azure-simplecmdb/blob/master/bin/cmdb-1.0.jar
# creating stuff you need
mkdir -p ./scripts/results
cd scripts
wget https://github.com/chrisvugrinec/azure-simplecmdb/blob/master/scripts/createOverview.sh
echo "Now create your cmdb with the createOverview.sh script...files will be stored in results"
echo "then open your jar....java -jar cmdb-1.0.jar"
