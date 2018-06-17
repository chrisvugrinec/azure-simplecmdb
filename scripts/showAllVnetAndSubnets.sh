#!/bin/bash


az account list -o tsv | while read LINE
do
  accountId=$(echo $LINE | awk '{ print $2 }')
  name=$(echo $LINE | awk '{ print $4 }')
  az account set --subscription $accountId
  echo "subscription:" $accountId "account name: "$name
  echo "====================================================================================="
  for rg in $(az group list | jq -r ".[].name")
  do
    echo "resourcegroup" $rg
    for vnet in $(az network vnet list -g $rg | jq -r ".[].name")
    do
       echo "vnet: "$vnet
       echo "subnets:"
       az network vnet subnet list --vnet-name $vnet -g $rg -o table
       echo "vm's:"
       az vm list -g $rg -d -o table
    done
  done
done
