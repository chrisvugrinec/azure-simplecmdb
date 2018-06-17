#!/bin/bash
  
az account list -o tsv | while read LINE
do
  accountId=$(echo $LINE | awk '{ print $2 }')
  name=$(echo $LINE | awk '{ print $4 }')
  az account set --subscription $accountId
  echo "subscription:" $accountId "account name: "$name
  az network nic list --query '[].{nic:name,machine:virtualMachine.id,group:resourceGroup,network:ipConfigurations[].subnet.id }' >results/$name-###-$accountId.json
done


