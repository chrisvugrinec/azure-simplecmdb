# azure-simplecmdb
This contains a simple cmdb to show your vms,vnets,subnets per Azure subscription.
It should look something like this:
![screenshot simple cmdb](docs/screenshot.png?raw=true "Screenshot Azure simple cmdb")

# Quickstart

## Getting your cmdb app
Go with your browser to the following page: https://github.com/chrisvugrinec/azure-simplecmdb/blob/master/bin/cmdb-1.0.jar and click the download button

## creating stuff you need
In the folder where your downloaded jar resides do the following:
- mkdir -p ./scripts/results
- cd scripts
- wget https://raw.githubusercontent.com/chrisvugrinec/azure-simplecmdb/master/scripts/createOverview.sh
- chmod 750 createOverview.sh
- Now create your cmdb with the createOverview.sh script...files will be stored in results folder
- open your jar....java -jar cmdb-1.0.jar
